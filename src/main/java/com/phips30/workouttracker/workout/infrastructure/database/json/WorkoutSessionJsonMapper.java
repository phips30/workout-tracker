package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSessionEntry;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSessionJsonMapper {
    WorkoutSession toDomain(WorkoutSessionDbEntity entity) {
        return WorkoutSession.of(
                new EntityId(entity.getId()),
                new EntityId(entity.getRoutineId()),
                entity.getStartedAt(),
                entity.getMetadata(),
                entity.getEntries().stream().map(this::toDomain).toList());
    }

    private WorkoutSessionEntry toDomain(WorkoutSessionEntryDbEntity entry) {
        return WorkoutSessionEntry.of(
                entry.getBlockPosition(),
                entry.getRound(),
                entry.getItemPosition(),
                RepetitionType.valueOf(entry.getRepetitionType()),
                entry.getCompletedRepetitions());
    }

    WorkoutSessionDbEntity toEntity(WorkoutSession workoutSession) {
        WorkoutSessionDbEntity entity = new WorkoutSessionDbEntity();
        entity.setId(workoutSession.getId().getId());
        entity.setRoutineId(workoutSession.getRoutineId().getId());
        entity.setStartedAt(workoutSession.getStartedAt());
        entity.setMetadata(workoutSession.getMetadata());
        entity.setEntries(workoutSession.getEntries().stream().map(this::toEntity).toList());
        return entity;
    }

    private WorkoutSessionEntryDbEntity toEntity(WorkoutSessionEntry entry) {
        WorkoutSessionEntryDbEntity entity = new WorkoutSessionEntryDbEntity();
        entity.setBlockPosition(entry.getBlockPosition());
        entity.setRound(entry.getRound());
        entity.setItemPosition(entry.getItemPosition());
        entity.setRepetitionType(entry.getType().name());
        entity.setCompletedRepetitions(entry.getCompletedRepetitions());
        return entity;
    }
}
