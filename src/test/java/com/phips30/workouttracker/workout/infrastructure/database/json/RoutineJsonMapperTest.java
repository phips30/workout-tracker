package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.phips30.workouttracker.RandomData.shortString;
import static org.junit.jupiter.api.Assertions.*;

class RoutineJsonMapperTest {

    private final RoutineJsonMapper mapper = new RoutineJsonMapper();

    private final Exercise exercise1 = new Exercise(new ExerciseName(shortString()));
    private final Exercise exercise2 = new Exercise(new ExerciseName(shortString()));

    private final Routine routine = Routine.of(
            EntityId.generate(),
            new RoutineName(shortString()),
            RoutineType.AMRAP,
            List.of(
                    RoutineBlock.of(1, 3, List.of(
                            RoutineBlockItem.of(1, exercise1, Repetition.of(10)),
                            RoutineBlockItem.of(2, exercise2, Repetition.of(RepetitionType.SECONDS, 30)))),
                    RoutineBlock.of(2, 2, List.of(
                            RoutineBlockItem.of(1, exercise1, Repetition.of(5))))));

    @Test
    void toEntity_mapsBlocksAndItems() {
        RoutineDbEntity entity = mapper.toEntity(routine);

        assertEquals(routine.getId().getId(), entity.getId());
        assertEquals(routine.getName().getValue(), entity.getName());
        assertEquals("AMRAP", entity.getRoutineType());
        assertEquals(2, entity.getBlocks().size());

        RoutineBlockDbEntity firstBlock = entity.getBlocks().getFirst();
        assertEquals(1, firstBlock.getPosition());
        assertEquals(3, firstBlock.getRounds());
        RoutineBlockItemDbEntity secondItem = firstBlock.getItems().get(1);
        assertEquals(2, secondItem.getPosition());
        assertEquals(exercise2.getId().getId(), secondItem.getExerciseId());
        assertEquals("SECONDS", secondItem.getRepetitionType());
        assertEquals(30, secondItem.getRepetitions());
    }

    @Test
    void toDomain_restoresRoutineFromEntity() {
        RoutineDbEntity entity = mapper.toEntity(routine);

        Routine restored = mapper.toDomain(entity, List.of(exercise2, exercise1));

        assertEquals(routine, restored);
        assertEquals(routine.getName(), restored.getName());
        assertEquals(routine.getRoutineType(), restored.getRoutineType());
        assertEquals(routine.getBlocks(), restored.getBlocks());
    }

    @Test
    void toDomain_exerciseMissing_throwsPersistenceException() {
        RoutineDbEntity entity = mapper.toEntity(routine);

        assertThrows(PersistenceException.class, () -> mapper.toDomain(entity, List.of(exercise1)));
    }
}
