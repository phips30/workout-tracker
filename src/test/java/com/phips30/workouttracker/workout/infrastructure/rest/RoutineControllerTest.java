package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.LoadRoutine;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateRoutine createRoutineUseCase;
    @MockitoBean
    private LoadRoutine loadRoutineUseCase;

    @Test
    public void addRoutine_doesNotExist_addedToDatabase_returns201() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        mvc.perform(
                        post("/api/routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(routine)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addRoutine_causesException_returns400() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new Exception(routine.name());
        }).when(createRoutineUseCase)
                .execute(any(), any(), any(), any());

        mvc.perform(post("/api/routine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(routine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getRoutine_routineFetchedProperly_returnsRoutinesAnd200() throws Exception {
        Routine routine = RoutineFactory.createRoutine().build();

        when(loadRoutineUseCase.loadRoutineWithWorkouts(routine.getName()))
                .thenReturn(routine);

        mvc.perform(get("/api/routine/" + routine.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(routine.getName()))
                .andExpect(jsonPath("$.routineType").value(routine.getRoutineType().toString()))
                .andExpect(jsonPath("$.exercises", hasSize(2)))
                .andExpect(jsonPath("$.repetitions", hasSize(2)));
    }

    @Test
    public void getRoutine_causesException_returns400() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doAnswer((invocation) -> {
            throw new Exception(routine.name());
        }).when(loadRoutineUseCase)
                .loadRoutineWithWorkouts(routine.name());

        mvc.perform(get("/api/routine/" + routine.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}