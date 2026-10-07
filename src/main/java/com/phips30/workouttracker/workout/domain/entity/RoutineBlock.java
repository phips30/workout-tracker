package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Part of a {@link Routine}: a set of items that is performed for a number of rounds.
 * Has no identity of its own. Items are kept ordered by their position.
 */
public final class RoutineBlock {
    private final int position;
    private final int rounds;
    private final List<RoutineBlockItem> items;

    private RoutineBlock(int position, int rounds, List<RoutineBlockItem> items) {
        if (position < 1) {
            throw new IllegalArgumentException("Position must be greater or equal to 1");
        }
        if (rounds < 1) {
            throw new IllegalArgumentException("Rounds must be greater or equal to 1");
        }
        AssertionHelper.assertNotNullOrEmpty(items, "Items is null or empty");
        if (items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Items contains null");
        }
        AssertionHelper.assertConsecutivePositions(
                items.stream().map(RoutineBlockItem::getPosition).toList(),
                "Item positions must be unique and start at 1 without gaps");

        this.position = position;
        this.rounds = rounds;
        this.items = items.stream().sorted(Comparator.comparingInt(RoutineBlockItem::getPosition)).toList();
    }

    public static RoutineBlock of(int position, int rounds, List<RoutineBlockItem> items) {
        return new RoutineBlock(position, rounds, items);
    }

    public int getPosition() {
        return position;
    }

    public int getRounds() {
        return rounds;
    }

    public List<RoutineBlockItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineBlock that = (RoutineBlock) o;
        return position == that.position && rounds == that.rounds && items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, rounds, items);
    }

    @Override
    public String toString() {
        return "RoutineBlock{" +
                "position=" + position +
                ", rounds=" + rounds +
                ", items=" + items +
                '}';
    }
}
