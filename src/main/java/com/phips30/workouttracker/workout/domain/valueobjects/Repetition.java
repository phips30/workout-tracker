package com.phips30.workouttracker.workout.domain.valueobjects;

public class Repetition {
    private static String NUMBER_TYPE = "NUMBER";

    private int number;
    private String type = NUMBER_TYPE; // Can either be seconds or number

    private Repetition(int number) {
        this.setNumber(number);
    }

    public static Repetition of(int number) {
        return new Repetition(number);
    }

    public void setNumber(int number) {
        if(number <= 1) {
            throw new IllegalArgumentException("Number must be greater or equal to 1");
        }
        this.number = number;
    }
}
