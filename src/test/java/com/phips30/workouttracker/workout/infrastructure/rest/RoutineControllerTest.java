package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.application.command.CreateRoutineCommand;
import com.phips30.workouttracker.workout.application.result.RoutineBlockItemResult;
import com.phips30.workouttracker.workout.application.result.RoutineBlockResult;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.result.RoutineResult;
import com.phips30.workouttracker.workout.application.usecase.RoutineService;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.phips30.workouttracker.UrlBuilder.buildUrl;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutineController.class)
class RoutineControllerTest {

    String endpointUrl = "/api/routine";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoutineService routineService;

    private CreateRoutineCommand toCommand(NewRoutineRequest routine) {
        return new CreateRoutineCommand(
                routine.name(),
                routine.routineType(),
                routine.blocks().stream()
                        .map(b -> new CreateRoutineCommand.Block(
                                b.position(),
                                b.rounds(),
                                b.items().stream()
                                        .map(i -> new CreateRoutineCommand.Item(
                                                i.position(), UUID.fromString(i.exerciseId()), i.repetitionType(), i.repetitions()))
                                        .toList()))
                        .toList());
    }

    @Test
    public void addRoutine_doesNotExist_addedToDatabase_returns201() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        mvc.perform(
                        post(endpointUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(routine)))
                .andExpect(status().isCreated());

        verify(routineService).createRoutine(toCommand(routine));
    }

    @Test
    public void addRoutine_alreadyExists_returns400() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new RoutineAlreadyExistsException(new RoutineName(routine.name()));
        }).when(routineService)
                .createRoutine(toCommand(routine));

        mvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void addRoutine_exerciseDoesNotExist_returns404() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new ExerciseNotFoundException(new EntityId(UUID.fromString(routine.blocks().getFirst().items().getFirst().exerciseId())));
        }).when(routineService)
                .createRoutine(toCommand(routine));

        mvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void addRoutine_causesServerError_returns500() throws Exception, RoutineAlreadyExistsException {
        String errorString = RandomData.shortString();
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new Exception(errorString);
        }).when(routineService)
                .createRoutine(any());

        mvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").value(errorString));
    }

    @Test
    public void getRoutines_routinesFetchedProperly_returnsRoutinesAnd200() throws Exception {
        List<RoutineResult> routines = List.of(
                RoutineFactory.createRoutineResult(),
                RoutineFactory.createRoutineResult());

        when(routineService.loadRoutines()).thenReturn(routines);

        mvc.perform(get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(routines.getFirst().name()))
                .andExpect(jsonPath("$[0].routineType").value(routines.getFirst().routineType()))
                .andExpect(jsonPath("$[1].name").value(routines.getLast().name()))
                .andExpect(jsonPath("$[1].routineType").value(routines.getLast().routineType()));
    }

    @Test
    public void getRoutineDetails_routineDetailsFetchedProperly_returnsDetailsAnd200() throws Exception {
        String routineName = RandomData.shortString();
        RoutineDetailResult details = RoutineFactory.createRoutineDetailResult();
        RoutineBlockResult block = details.blocks().getFirst();
        RoutineBlockItemResult item = block.items().getFirst();

        when(routineService.loadRoutine(routineName)).thenReturn(details);

        mvc.perform(get(buildUrl(endpointUrl, routineName, "detail"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocks", hasSize(1)))
                .andExpect(jsonPath("$.blocks[0].position").value(block.position()))
                .andExpect(jsonPath("$.blocks[0].rounds").value(block.rounds()))
                .andExpect(jsonPath("$.blocks[0].items", hasSize(2)))
                .andExpect(jsonPath("$.blocks[0].items[0].position").value(item.position()))
                .andExpect(jsonPath("$.blocks[0].items[0].exercise.id").value(item.exercise().id()))
                .andExpect(jsonPath("$.blocks[0].items[0].exercise.name").value(item.exercise().name()))
                .andExpect(jsonPath("$.blocks[0].items[0].repetition.number").value(item.repetition().number()))
                .andExpect(jsonPath("$.blocks[0].items[0].repetition.type").value(item.repetition().type()));
    }

    @Test
    public void getRoutine_notFound_returns404() throws Exception {
        String routineName = RandomData.shortString();
        doAnswer((invocation) -> {
            throw new RoutineNotFoundException(new RoutineName(routineName));
        }).when(routineService).loadRoutine(routineName);

        mvc.perform(get(buildUrl(endpointUrl, routineName, "detail"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getRoutine_causesServerError_returns500() throws Exception {
        String errorString = RandomData.shortString();
        String routineName = RandomData.shortString();
        doAnswer((invocation) -> {
            throw new Exception(errorString);
        }).when(routineService).loadRoutine(routineName);

        mvc.perform(get(buildUrl(endpointUrl, routineName, "detail"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").value(errorString));
    }
}
