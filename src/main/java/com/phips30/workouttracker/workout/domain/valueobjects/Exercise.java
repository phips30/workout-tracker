package com.phips30.workouttracker.workout.domain.valueobjects;

public class Exercise {
    private String name;

    public Exercise(String name) {
        this.setName(name);
    }

    public void setName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is null or empty");
        }
        this.name = name;
    }
}
