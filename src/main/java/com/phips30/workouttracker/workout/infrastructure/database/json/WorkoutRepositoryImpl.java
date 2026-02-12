package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class WorkoutRepositoryImpl implements WorkoutRepository {

    @Override
    public Set<Workout> loadWorkoutsForRoutine(Routine routine) {
        return Set.of();
    }

    @Override
    public Workout save(Routine routine, Workout workout) {
        return null;
    }
}
