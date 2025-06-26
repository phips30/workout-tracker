package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.LoadRoutine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseSetup {

    @Bean
    public CreateRoutine createRoutineUseCase(RoutineRepository routineRepository) {
        return new CreateRoutine(routineRepository);
    }

    @Bean
    public LoadRoutine loadRoutineUseCase(RoutineRepository routineRepository, WorkoutRepository workoutRepository) {
        return new LoadRoutine(routineRepository, workoutRepository);
    }
}
