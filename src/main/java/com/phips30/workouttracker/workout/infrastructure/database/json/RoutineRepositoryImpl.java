package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RoutineRepositoryImpl implements RoutineRepository {

    private final ObjectMapper objectMapper;

    private final JsonDatabaseConfig jsonDatabaseConfig;
    private final ExerciseRepository exerciseRepository;
    private final RoutineJsonMapper routineJsonMapper;

    public RoutineRepositoryImpl(ObjectMapper objectMapper,
                                 JsonDatabaseConfig jsonDatabaseConfig,
                                 ExerciseRepository exerciseRepository,
                                 RoutineJsonMapper routineJsonMapper) {
        this.objectMapper = objectMapper;
        this.jsonDatabaseConfig = jsonDatabaseConfig;
        this.exerciseRepository = exerciseRepository;
        this.routineJsonMapper = routineJsonMapper;
    }

    @Override
    public Optional<Routine> loadRoutine(RoutineName routineName) {
        try {
            return readRoutines().stream()
                    .filter(routine -> Objects.equals(routine.getName(), routineName.getValue()))
                    .findFirst()
                    .map(r -> {
                        List<Exercise> e = loadExercisesForRoutine(r);
                        return routineJsonMapper.toDomain(r, e);
                    });
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to load routine '%s'", routineName), e);
        }
    }

    @Override
    public List<Routine> loadRoutines() {
        try {
            return readRoutines().stream()
                    .map(r -> {
                        List<Exercise> e = loadExercisesForRoutine(r);
                        return routineJsonMapper.toDomain(r, e);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenceException("Failed to load routines", e);
        }
    }

    private List<Exercise> loadExercisesForRoutine(RoutineDbEntity routine) {
        return exerciseRepository.loadByIds(routine.getExerciseIds().stream().map(EntityId::new).toList());
    }

    @Override
    public boolean exists(RoutineName name) {
        // TODO: just check for name without loading entire object
        return loadRoutine(name).isPresent();
    }

    @Override
    public void saveRoutine(Routine routine) {
        try {
            List<RoutineDbEntity> routineDbEntities = readRoutines();
            routineDbEntities.add(routineJsonMapper.toEntity(routine));
            objectMapper.writeValue(routineDbFile(), routineDbEntities);
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to save routine '%s'", routine.getName()), e);
        }
    }

    private File routineDbFile() {
        return new File(jsonDatabaseConfig.getJson().getRoutineFilepath());
    }

    /**
     * Reads all stored routines. A completely empty file is treated as an empty database.
     */
    private List<RoutineDbEntity> readRoutines() throws IOException {
        File routineDbFile = routineDbFile();
        if (routineDbFile.exists() && routineDbFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(
                routineDbFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineDbEntity.class));
    }
}
