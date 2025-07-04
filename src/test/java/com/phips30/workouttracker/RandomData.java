package com.phips30.workouttracker;

import com.github.javafaker.Faker;

public class RandomData {

    public static Faker Faker() {
        return new Faker();
    }

    public static String shortString() {
        return Faker().lorem().fixedString(5);
    }

    public static int digit() {
        return Faker().number().randomDigit();
    }

    public static int positiveDigit() {
        return Faker().number().randomDigitNotZero();
    }
}
