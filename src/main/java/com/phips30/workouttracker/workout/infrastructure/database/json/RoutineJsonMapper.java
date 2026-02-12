package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoutineJsonMapper {
    Routine toDomain(RoutineDbEntity entity, List<Exercise> exercises) {
        return Routine.of(
                new EntityId(entity.getId()),
                new RoutineName(entity.getName()),
                RoutineType.valueOf(entity.getRoutineType()),
                exercises,
                entity.getRepetitions().stream()
                        .map(Repetition::of)
                        .toList()
        );
    }

    RoutineDbEntity toEntity(Routine routine) {
        RoutineDbEntity routineToSave = new RoutineDbEntity();
        routineToSave.setId(routine.getId().getId());
        routineToSave.setName(routine.getName().getValue());
        routineToSave.setRoutineType(routine.getRoutineType().name());
        routineToSave.setExerciseIds(
                routine.getExercises().stream()
                        .map(Exercise::getId)
                        .map(EntityId::getId)
                        .toList()
        );
        routineToSave.setRepetitions(
                routine.getRepetitions().stream()
                        .map(Repetition::getNumber)
                        .toList()
        );
        return routineToSave;
    }
}
