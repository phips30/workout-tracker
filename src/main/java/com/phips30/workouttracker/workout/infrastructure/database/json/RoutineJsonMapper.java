package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoutineJsonMapper {
    Routine toDomain(RoutineDbEntity entity, List<Exercise> exercises) {
        Map<UUID, Exercise> exercisesById = exercises.stream()
                .collect(Collectors.toMap(e -> e.getId().getId(), Function.identity(), (first, second) -> first));
        return Routine.of(
                new EntityId(entity.getId()),
                new RoutineName(entity.getName()),
                RoutineType.valueOf(entity.getRoutineType()),
                entity.getBlocks().stream().map(block -> toDomain(block, exercisesById, entity)).toList()
        );
    }

    private RoutineBlock toDomain(RoutineBlockDbEntity block, Map<UUID, Exercise> exercisesById, RoutineDbEntity routine) {
        return RoutineBlock.of(
                block.getPosition(),
                block.getRounds(),
                block.getItems().stream().map(item -> toDomain(item, exercisesById, routine)).toList());
    }

    private RoutineBlockItem toDomain(RoutineBlockItemDbEntity item, Map<UUID, Exercise> exercisesById, RoutineDbEntity routine) {
        Exercise exercise = exercisesById.get(item.getExerciseId());
        if (exercise == null) {
            throw new PersistenceException(
                    String.format("Exercise %s of routine '%s' does not exist", item.getExerciseId(), routine.getName()),
                    null);
        }
        return RoutineBlockItem.of(
                item.getPosition(),
                exercise,
                Repetition.of(RepetitionType.valueOf(item.getRepetitionType()), item.getRepetitions()));
    }

    RoutineDbEntity toEntity(Routine routine) {
        RoutineDbEntity routineToSave = new RoutineDbEntity();
        routineToSave.setId(routine.getId().getId());
        routineToSave.setName(routine.getName().getValue());
        routineToSave.setRoutineType(routine.getRoutineType().name());
        routineToSave.setBlocks(routine.getBlocks().stream().map(this::toEntity).toList());
        return routineToSave;
    }

    private RoutineBlockDbEntity toEntity(RoutineBlock block) {
        RoutineBlockDbEntity blockToSave = new RoutineBlockDbEntity();
        blockToSave.setPosition(block.getPosition());
        blockToSave.setRounds(block.getRounds());
        blockToSave.setItems(block.getItems().stream().map(this::toEntity).toList());
        return blockToSave;
    }

    private RoutineBlockItemDbEntity toEntity(RoutineBlockItem item) {
        RoutineBlockItemDbEntity itemToSave = new RoutineBlockItemDbEntity();
        itemToSave.setPosition(item.getPosition());
        itemToSave.setExerciseId(item.getExercise().getId().getId());
        itemToSave.setRepetitionType(item.getRepetition().getType().name());
        itemToSave.setRepetitions(item.getRepetition().getNumber());
        return itemToSave;
    }
}
