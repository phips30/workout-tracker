package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.ExerciseResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineRespone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.phips30.workouttracker.RandomData.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoutineControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addRoutine_doesNotExist_addedToDatabase() {
        ResponseEntity<ExerciseResponse> exercise1 = restTemplate
                .postForEntity("/api/exercise", new NewExerciseRequest(randomString(10)), ExerciseResponse.class);
        ResponseEntity<ExerciseResponse> exercise2 = restTemplate
                .postForEntity("/api/exercise", new NewExerciseRequest(randomString(10)), ExerciseResponse.class);

        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest(
                List.of(exercise1.getBody().id(), exercise2.getBody().id()),
                List.of(RandomData.positiveDigit(), RandomData.positiveDigit()));

        ResponseEntity<Void> postReponse = restTemplate.postForEntity("/api/routine", routine, Void.class);
        assertThat(postReponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List<RoutineRespone>> getResponse = restTemplate
                .exchange("/api/routine", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(getResponse.getBody().stream().anyMatch(r -> routine.name().equals(r.name())));
    }
}