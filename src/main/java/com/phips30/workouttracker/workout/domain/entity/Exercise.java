package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
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
        this(EntityId.generate(), name);
    }

    public Exercise(EntityId id, ExerciseName name) {
        AssertionHelper.assertNotNull(id, "EntityId is null");
        AssertionHelper.assertNotNull(name, "ExerciseName is null");

        this.id = id;
        this.name = name;
    }

    public EntityId getId() {
        return id;
    }

    public ExerciseName getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean equalObjects = false;

        if (otherObject != null && this.getClass() == otherObject.getClass()) {
            Exercise exercise = (Exercise) otherObject;
            equalObjects = this.id.equals(exercise.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
