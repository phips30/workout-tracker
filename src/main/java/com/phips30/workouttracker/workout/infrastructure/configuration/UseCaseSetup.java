package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.application.usecase.ExerciseService;
import com.phips30.workouttracker.workout.application.usecase.RoutineService;
import com.phips30.workouttracker.workout.application.usecase.WorkoutSessionService;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutSessionRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.service.RoutineFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseSetup {

    @Bean
    public ExerciseFactory exerciseFactory(ExerciseRepository exerciseRepository) {
        return new ExerciseFactory(exerciseRepository);
    }

    @Bean
    public RoutineFactory routineFactory(RoutineRepository routineRepository, ExerciseRepository exerciseRepository) {
        return new RoutineFactory(routineRepository, exerciseRepository);
    }

    @Bean
    public RoutineService routineService(RoutineRepository routineRepository, RoutineFactory routineFactory) {
        return new RoutineService(routineRepository, routineFactory);
    }

    @Bean
    public ExerciseService exerciseService(ExerciseRepository exerciseRepository, ExerciseFactory exerciseFactory) {
        return new ExerciseService(exerciseRepository, exerciseFactory);
    }

    @Bean
    public WorkoutSessionService workoutSessionService(RoutineRepository routineRepository, WorkoutSessionRepository workoutSessionRepository) {
        return new WorkoutSessionService(routineRepository, workoutSessionRepository);
    }
}
