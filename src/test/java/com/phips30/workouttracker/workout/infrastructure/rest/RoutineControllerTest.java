package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.UrlBuilder;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.usecase.RoutineService;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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

    @MockitoBean
    private RoutineService routineService;

    @Test
    public void addRoutine_doesNotExist_addedToDatabase_returns201() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        mvc.perform(
                        post(endpointUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(routine)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addRoutine_causesException_returns400() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new Exception(routine.name());
        }).when(routineService)
                .createRoutine(any(), any(), any(), any());

        mvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(routine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getRoutines_routineFetchedProperly_returnsRoutinesAnd200() throws Exception {
        List<Routine> routines = List.of(
                RoutineFactory.createRoutine().build(),
                RoutineFactory.createRoutine().build());

        when(routineService.loadRoutines()).thenReturn(routines);

        mvc.perform(get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(routines.getFirst().getName().getValue()))
                .andExpect(jsonPath("$[0].routineType").value(routines.getFirst().getRoutineType().toString()))
                .andExpect(jsonPath("$[1].name").value(routines.getLast().getName().getValue()))
                .andExpect(jsonPath("$[1].routineType").value(routines.getLast().getRoutineType().toString()));
    }

    @Test
    public void getRoutineDetails_routineDetailsFetchedProperly_returnsDetailsAnd200() throws Exception {
        Routine routine = RoutineFactory.createRoutine().build();

        when(routineService.loadRoutine(routine.getName()))
                .thenReturn(routine);

        mvc.perform(get(UrlBuilder.buildUrl(endpointUrl, routine.getName().getValue(), "/detail"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exercises", hasSize(2)))
                .andExpect(jsonPath("$.repetitions", hasSize(2)));
    }

    @Test
    public void getRoutine_causesException_returns400() throws Exception {
        Routine routine = RoutineFactory.createRoutine().build();
        doAnswer((invocation) -> {
            throw new Exception(routine.getName().getValue());
        }).when(routineService)
                .loadRoutine(routine.getName());

        mvc.perform(get(UrlBuilder.buildUrl(endpointUrl, routine.getName().getValue(), "/detail"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}