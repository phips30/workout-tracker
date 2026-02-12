package com.phips30.workouttracker.workout.infrastructure.configuration;

import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseService;
import com.phips30.workouttracker.workout.domain.usecase.RoutineService;
import com.phips30.workouttracker.workout.domain.usecase.WorkoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseSetup {

    @Bean
    public RoutineService routineService(RoutineRepository routineRepository, ExerciseService exerciseService) {
        return new RoutineService(routineRepository, exerciseService);
    }

    @Bean
    public ExerciseService exerciseService(ExerciseRepository exerciseRepository) {
        return new ExerciseService(exerciseRepository);
    }

    @Bean
    public WorkoutService workoutService(RoutineRepository routineRepository, WorkoutRepository workoutRepository) {
        return new WorkoutService(routineRepository, workoutRepository);
    }
}
