package com.phips30.workouttracker;

import com.github.javafaker.Faker;

import java.util.UUID;

public class RandomData {

    public static Faker Faker() {
        return new Faker();
    }

    public static UUID randomUUID() {
        return UUID.randomUUID();
    }

    public static String shortString() {
        return Faker().lorem().fixedString(5);
    }

    public static String randomString(int length) {
        return Faker().lorem().fixedString(length);
    }

    public static int digit() {
        return Faker().number().randomDigit();
    }

    public static int positiveDigit() {
        return Faker().number().randomDigitNotZero();
    }
}
