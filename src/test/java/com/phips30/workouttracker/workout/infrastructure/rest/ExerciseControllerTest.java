package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.TestDataGenerator.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest {


    String endpointUrl = "/api/exercise";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExerciseService exerciseService;

    @Test
    public void getExercises_throwsError_returns500() throws Exception {
        String errorString = RandomData.shortString();
        doAnswer((invocation) -> {
            throw new Exception(errorString);
        }).when(exerciseService).loadAll();

        mvc.perform(get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").value(errorString));
    }

    @Test
    public void getExercises_exercisesFetchedProperly_returnsExercisesAnd200() throws Exception {
        List<Exercise> exercises = List.of(
                ExerciseFactory.createExercise().build(),
                ExerciseFactory.createExercise().build());

        when(exerciseService.loadAll()).thenReturn(exercises);

        mvc.perform(get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(exercises.getFirst().getId().getId().toString()))
                .andExpect(jsonPath("$[0].name").value(exercises.getFirst().getName().toString()))
                .andExpect(jsonPath("$[1].id").value(exercises.getLast().getId().getId().toString()))
                .andExpect(jsonPath("$[1].name").value(exercises.getLast().getName().toString()));
    }

}