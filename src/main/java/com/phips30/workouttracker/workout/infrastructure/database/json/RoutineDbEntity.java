package com.phips30.workouttracker.workout.infrastructure.database.json;

import java.util.List;
import java.util.UUID;

class RoutineDbEntity {
    private UUID id;
    private String name;
    private String routineType;
    private List<RoutineBlockDbEntity> blocks;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public List<RoutineBlockDbEntity> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<RoutineBlockDbEntity> blocks) {
        this.blocks = blocks;
    }
}
