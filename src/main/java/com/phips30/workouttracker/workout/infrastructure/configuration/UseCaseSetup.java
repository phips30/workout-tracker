package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseApplicationService;
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
    public ExerciseApplicationService exerciseApplicationService(ExerciseRepository exerciseRepository) {
        ExerciseFactory exerciseFactory = new ExerciseFactory(exerciseRepository);
        return new ExerciseApplicationService(exerciseFactory, exerciseRepository);
    }
}
