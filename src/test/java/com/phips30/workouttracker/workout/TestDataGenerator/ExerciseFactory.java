package com.phips30.workouttracker.workout.TestDataGenerator;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

public class ExerciseFactory {

    private Exercise exercise;

    public static ExerciseFactory createExercise() {
        return createExercise(RandomData.shortString());
    }

    public static ExerciseFactory createExercise(String name) {
        ExerciseFactory exerciseFactory = new ExerciseFactory();
        exerciseFactory.exercise = new Exercise(new ExerciseName(name));
        return exerciseFactory;
    }

    public Exercise build() {
        return this.exercise;
    }
}
