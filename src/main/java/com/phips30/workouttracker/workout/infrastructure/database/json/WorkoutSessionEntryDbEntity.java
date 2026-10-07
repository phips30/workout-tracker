package com.phips30.workouttracker.workout.infrastructure.database.json;

class WorkoutSessionEntryDbEntity {
    private int blockPosition;
    private int round;
    private int itemPosition;
    private String repetitionType;
    private int completedRepetitions;

    public int getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(int blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public String getRepetitionType() {
        return repetitionType;
    }

    public void setRepetitionType(String repetitionType) {
        this.repetitionType = repetitionType;
    }

    public int getCompletedRepetitions() {
        return completedRepetitions;
    }

    public void setCompletedRepetitions(int completedRepetitions) {
        this.completedRepetitions = completedRepetitions;
    }
}
