package com.phips30.workouttracker.workout.application.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record CreateWorkoutSessionCommand(String routineName,
                                          LocalDateTime startedAt,
                                          Map<String, Object> metadata,
                                          List<Entry> entries) {

    /**
     * What was done for one item of the routine in one round: the completed repetitions for items counted
     * in repetitions, whether it was completed for items measured in seconds.
     */
    public record Entry(int blockPosition,
                        int round,
                        int itemPosition,
                        int completedRepetitions,
                        boolean completed) {
    }
}
