package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.LoadRoutine;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void addRoutine_alreadyExists_returns400() throws Exception {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        doThrow(new RuntimeException())
                .when(createRoutineUseCase)
                .execute(any(), any(), any(), any());

        mvc.perform(
                post("/api/routine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(routine)))
                .andExpect(status().isBadRequest());
    }
}