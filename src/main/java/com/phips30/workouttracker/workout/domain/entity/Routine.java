package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate root routine
 */
public class Routine {
    private final EntityId id;
    private final RoutineName name;
    private final RoutineType routineType;
    private final List<RoutineBlock> blocks;

    private Routine(EntityId id, RoutineName name, RoutineType routineType, List<RoutineBlock> blocks) {
        AssertionHelper.assertNotNull(id, "Entity id is null");
        AssertionHelper.assertNotNull(name, "RoutineName id is null");
        AssertionHelper.assertNotNull(routineType, "RoutineType is null");
        AssertionHelper.assertNotNullOrEmpty(blocks, "Blocks is null or empty");
        if (blocks.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Blocks contains null");
        }
        AssertionHelper.assertConsecutivePositions(
                blocks.stream().map(RoutineBlock::getPosition).toList(),
                "Block positions must be unique and start at 1 without gaps");

        this.id = id;
        this.name = name;
        this.routineType = routineType;
        this.blocks = blocks.stream().sorted(Comparator.comparingInt(RoutineBlock::getPosition)).toList();
    }

    public static Routine createNew(
            RoutineName name,
            RoutineType routineType,
            List<RoutineBlock> blocks) {
        return new Routine(EntityId.generate(), name, routineType, blocks);
    }

    public static Routine of(
            EntityId id,
            RoutineName name,
            RoutineType routineType,
            List<RoutineBlock> blocks) {
        return new Routine(id, name, routineType, blocks);
    }

    public EntityId getId() {
        return id;
    }

    public List<RoutineBlock> getBlocks() {
        return blocks;
    }

    public RoutineName getName() {
        return this.name;
    }

    public RoutineType getRoutineType() {
        return routineType;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean equalObjects = false;

        if (otherObject != null && this.getClass() == otherObject.getClass()) {
            Routine routine = (Routine) otherObject;
            equalObjects = this.id.equals(routine.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Routine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", routineType=" + routineType +
                '}';
    }
}
