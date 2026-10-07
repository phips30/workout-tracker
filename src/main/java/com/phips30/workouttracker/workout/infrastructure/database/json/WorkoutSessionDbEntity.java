package com.phips30.workouttracker.workout.infrastructure.database.json;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class WorkoutSessionDbEntity {
    private UUID id;
    private UUID routineId;
    private LocalDateTime startedAt;
    private Map<String, Object> metadata;
    private List<WorkoutSessionEntryDbEntity> entries;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRoutineId() {
        return routineId;
    }

    public void setRoutineId(UUID routineId) {
        this.routineId = routineId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<WorkoutSessionEntryDbEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<WorkoutSessionEntryDbEntity> entries) {
        this.entries = entries;
    }
}
