package com.phips30.workouttracker.workout.infrastructure.database.json;

import java.util.List;

class RoutineBlockDbEntity {
    private int position;
    private int rounds;
    private List<RoutineBlockItemDbEntity> items;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public List<RoutineBlockItemDbEntity> getItems() {
        return items;
    }

    public void setItems(List<RoutineBlockItemDbEntity> items) {
        this.items = items;
    }
}
