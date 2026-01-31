package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.infrastructure.rest.dto.ExerciseResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.phips30.workouttracker.RandomData.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExerciseControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addExercise_doesNotExist_addedToDatabase() {
        NewExerciseRequest exercise = new NewExerciseRequest(randomString(10));
        ResponseEntity<ExerciseResponse> postReponse = restTemplate.postForEntity("/api/exercise", exercise, ExerciseResponse.class);
        assertThat(postReponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List<ExerciseResponse>> getResponse = restTemplate
                .exchange(postReponse.getHeaders().get(HttpHeaders.LOCATION).getFirst(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(getResponse.getBody().stream().anyMatch(e -> exercise.name().equals(e.name())));
    }
}