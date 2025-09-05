package com.phips30.workouttracker.workout.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public final class EntityId {
    private final UUID id;

    public EntityId(UUID id) {
        this.id = Objects.requireNonNull(id);
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

    public static EntityId generate() {
        return new EntityId(UUID.randomUUID());
    }
}
