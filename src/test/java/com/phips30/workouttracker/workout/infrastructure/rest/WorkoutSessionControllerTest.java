package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.application.command.CreateWorkoutSessionCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutSessionEntryResult;
import com.phips30.workouttracker.workout.application.result.WorkoutSessionResult;
import com.phips30.workouttracker.workout.application.usecase.WorkoutSessionService;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutSessionRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.WorkoutSessionEntryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.phips30.workouttracker.RandomData.shortString;
import static com.phips30.workouttracker.UrlBuilder.buildUrl;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutSessionController.class)
class WorkoutSessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutSessionService workoutSessionService;

    String routineName = shortString();
    String endpointUrl = "/api/routine/%s/workout-session";

    private final LocalDateTime startedAt = LocalDateTime.now();

    NewWorkoutSessionRequest newWorkoutSessionRequest = new NewWorkoutSessionRequest(
            startedAt,
            Map.of("weight", 10),
            List.of(new WorkoutSessionEntryRequest(1, 1, 1, 8, null),
                    new WorkoutSessionEntryRequest(1, 1, 2, null, true)));

    @Test
    public void createWorkoutSession_added_returns201() throws Exception {
        mvc.perform(post(buildUrl(endpointUrl, routineName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWorkoutSessionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));

        verify(workoutSessionService).saveWorkoutSession(new CreateWorkoutSessionCommand(
                routineName,
                startedAt,
                Map.of("weight", 10),
                List.of(new CreateWorkoutSessionCommand.Entry(1, 1, 1, 8, false),
                        new CreateWorkoutSessionCommand.Entry(1, 1, 2, 0, true))));
    }

    @Test
    public void createWorkoutSession_routineNotFound_returns404() throws Exception {
        doThrow(new RoutineNotFoundException(new RoutineName(routineName)))
                .when(workoutSessionService)
                .saveWorkoutSession(any());

        mvc.perform(post(buildUrl(endpointUrl, routineName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWorkoutSessionRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getWorkoutSessions_returnsSessionsAnd200() throws Exception {
        WorkoutSessionResult session = new WorkoutSessionResult(
                UUID.randomUUID().toString(),
                startedAt,
                Map.of("weight", 10),
                List.of(new WorkoutSessionEntryResult(1, 1, 1, "NUMBER", 8, null),
                        new WorkoutSessionEntryResult(1, 1, 2, "SECONDS", null, true)));
        when(workoutSessionService.loadWorkoutSessionsForRoutine(routineName)).thenReturn(List.of(session));

        mvc.perform(get(buildUrl(endpointUrl, routineName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(session.id()))
                .andExpect(jsonPath("$[0].metadata.weight").value(10))
                .andExpect(jsonPath("$[0].entries.length()").value(2))
                .andExpect(jsonPath("$[0].entries[0].repetitionType").value("NUMBER"))
                .andExpect(jsonPath("$[0].entries[0].completedRepetitions").value(8))
                .andExpect(jsonPath("$[0].entries[1].repetitionType").value("SECONDS"))
                .andExpect(jsonPath("$[0].entries[1].completed").value(true));
    }

    @Test
    public void getWorkoutSessions_routineNotFound_returns404() throws Exception {
        when(workoutSessionService.loadWorkoutSessionsForRoutine(routineName))
                .thenThrow(new RoutineNotFoundException(new RoutineName(routineName)));

        mvc.perform(get(buildUrl(endpointUrl, routineName)))
                .andExpect(status().isNotFound());
    }
}
