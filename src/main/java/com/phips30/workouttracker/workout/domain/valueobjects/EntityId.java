package com.phips30.workouttracker.workout.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public final class EntityId {
    private final UUID id;

    private EntityId(UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    public static EntityId generate() {
        return EntityId.of(UUID.randomUUID());
    }

    public static EntityId of(UUID id) {
        return new EntityId(id);
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId id = (EntityId) o;
        return Objects.equals(this.id, id.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
