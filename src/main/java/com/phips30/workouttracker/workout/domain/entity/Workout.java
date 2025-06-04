package com.phips30.workouttracker.workout.domain.entity;


import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.apache.commons.lang3.tuple.Pair;
import java.time.LocalDateTime;
import java.util.List;

public class Workout {
    private Routine routine;
    private LocalDateTime completedAt;
    private List<Round> rounds;
    private List<Pair<String, String>> metadata;
}
