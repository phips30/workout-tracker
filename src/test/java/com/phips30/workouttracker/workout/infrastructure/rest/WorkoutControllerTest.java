package com.phips30.workouttracker.workout.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.usecase.WorkoutService;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.phips30.workouttracker.RandomData.shortString;
import static com.phips30.workouttracker.UrlBuilder.buildUrl;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutService workoutService;

    String routineName = shortString();
    String endpointUrl = "/api/routines/%s/workouts";

    private String generateUrl(String url, String value) {
        return String.format(url, value);
    }


    Workout workout = Workout.of(
            LocalDateTime.now(),
            List.of(new Round(Duration.ofMinutes(10))),
            List.of()
    );

    NewWorkoutRequest newWorkoutRequest = new NewWorkoutRequest(
            LocalDateTime.now(),
            List.of(new Round(Duration.ofMinutes(10))),
            List.of());

    @Test
    public void addWorkout_addedToDatabase_returns201() throws Exception {

        mvc.perform(post(buildUrl(endpointUrl, routineName))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newWorkoutRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));

        verify(workoutService).saveWorkout(eq(routineName), any(Workout.class));
    }

    @Test
    public void addWorkout_routineDoesnNotExist_returns400() throws Exception {
        doThrow(new RoutineNotFoundException(new RoutineName(routineName)))
                .when(workoutService)
                .saveWorkout(eq(routineName), any());

        mvc.perform(post(buildUrl(endpointUrl, routineName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(""))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWorkouts_workoutsFetchedProperly_returnsWorkoutsAnd200() throws Exception {
        List<Workout> workouts = List.of(workout);

        when(workoutService.loadWorkoutsForRoutine(routineName)).thenReturn(workouts);

        mvc.perform(get(buildUrl(endpointUrl, routineName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(workoutService).loadWorkoutsForRoutine(routineName);
    }
}