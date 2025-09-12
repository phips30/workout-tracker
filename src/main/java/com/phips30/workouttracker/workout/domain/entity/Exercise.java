package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

/***
 * Exercise Entity
 * Treating it as an entity instead of value object since it can live standalone and is mutable
 */
public class Exercise {
    private final EntityId id;
    private final ExerciseName name;

    public Exercise(ExerciseName name) {
        this.id = EntityId.generate();
        this.name = name;
    }

    public ExerciseName getName() {
        return this.name;
    }
}
