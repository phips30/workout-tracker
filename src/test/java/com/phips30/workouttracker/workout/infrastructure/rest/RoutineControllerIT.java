package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineRespone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoutineControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addRoutine_doesNotExist_addedToDatabase() {
        NewRoutineRequest routine = RoutineFactory.createNewRoutineRequest();
        ResponseEntity<Void> postReponse = restTemplate.postForEntity("/api/routine", routine, Void.class);
        assertThat(postReponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<RoutineRespone> getResponse = restTemplate.getForEntity("/api/routine/"  + routine.name(), RoutineRespone.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(getResponse.getBody());
        assertEquals(getResponse.getBody().name(), routine.name());
        assertEquals(getResponse.getBody().routineType(), routine.routineType().toString());
    }
}