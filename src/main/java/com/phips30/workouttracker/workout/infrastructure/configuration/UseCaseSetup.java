package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseService;
import com.phips30.workouttracker.workout.domain.usecase.RoutineService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseSetup {

    @Bean
    public RoutineService routineService(RoutineRepository routineRepository) {
        return new RoutineService(routineRepository);
    }

    @Bean
    public ExerciseService exerciseService(ExerciseRepository exerciseRepository) {
        return new ExerciseService(exerciseRepository);
    }
}
