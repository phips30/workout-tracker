package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseApplicationService;
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
    public LoadRoutine loadRoutineUseCase(RoutineRepository routineRepository) {
        return new LoadRoutine(routineRepository);
    }

    @Bean
    public ExerciseApplicationService exerciseApplicationService(ExerciseRepository exerciseRepository) {
        ExerciseFactory exerciseFactory = new ExerciseFactory(exerciseRepository);
        return new ExerciseApplicationService(exerciseFactory, exerciseRepository);
    }
}
