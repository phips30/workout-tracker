package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ExerciseRepositoryImpl implements ExerciseRepository {

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public Exercise create(Exercise exercise) {
        return null;
    }
}
