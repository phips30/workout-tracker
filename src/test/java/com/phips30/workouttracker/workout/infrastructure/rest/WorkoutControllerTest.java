package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.UrlBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mvc;

    String workoutName = RandomData.shortString();
    String endpointUrl = "/api/workout";

    @Test
    public void addWorkout_addedToDatabase_returns201() throws Exception {
        mvc.perform(post(UrlBuilder.buildUrl(endpointUrl, workoutName))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(""))
                .andExpect(status().isCreated());
    }

    // TODO: Test all exceptions properly and not just the generic one
    @Test
    public void addWorkout_causesException_returns400() throws Exception {
        mvc.perform(post(UrlBuilder.buildUrl(endpointUrl, workoutName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(""))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getWorkouts_workoutsFetchedProperly_returnsWorkoutsAnd200() throws Exception {
        mvc.perform(get(UrlBuilder.buildUrl(endpointUrl, workoutName))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getWorkouts_causesException_returns400() throws Exception {
        mvc.perform(get(UrlBuilder.buildUrl(endpointUrl, workoutName))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}