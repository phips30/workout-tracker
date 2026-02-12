package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RoutineRepositoryImpl implements RoutineRepository {

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RoutineRepositoryImpl.class);

    private final JsonDatabaseConfig jsonDatabaseConfig;
    private final ExerciseRepositoryImpl exerciseRepository;
    private final RoutineJsonMapper routineJsonMapper;

    public RoutineRepositoryImpl(ObjectMapper objectMapper,
                                 JsonDatabaseConfig jsonDatabaseConfig,
                                 ExerciseRepositoryImpl exerciseRepository,
                                 RoutineJsonMapper routineJsonMapper) {
        this.objectMapper = objectMapper;
        this.jsonDatabaseConfig = jsonDatabaseConfig;
        this.exerciseRepository = exerciseRepository;
        this.routineJsonMapper = routineJsonMapper;
    }

    @Override
    public Optional<Routine> loadRoutine(RoutineName routineName) {
        try {
            Set<RoutineDbEntity> routineDbEntities = objectMapper.readValue(
                    new File(jsonDatabaseConfig.getJson().getRoutineFilepath()),
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, RoutineDbEntity.class));

            return routineDbEntities.stream()
                    .filter(routine -> Objects.equals(routine.getName(), routineName.getValue()))
                    .findFirst()
                    .map(r -> {
                        List<Exercise> e = loadExercisesForRoutine(r);
                        return routineJsonMapper.toDomain(r, e);
                    });
        } catch (IOException e) {
            logger.error("Error parsing the json file for routine name '{}'", routineName, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Routine> loadRoutines() {
        try {
            List<RoutineDbEntity> routineDbEntities = objectMapper.readValue(
                    new File(jsonDatabaseConfig.getJson().getRoutineFilepath()),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineDbEntity.class));

            return routineDbEntities.stream()
                    .map(r -> {
                        List<Exercise> e = loadExercisesForRoutine(r);
                        return routineJsonMapper.toDomain(r, e);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error parsing the json file", e);
        }
        return new ArrayList<>();
    }

    private List<Exercise> loadExercisesForRoutine(RoutineDbEntity routine) {
        return exerciseRepository.loadByIds(routine.getExerciseIds());
    }

    @Override
    public boolean exists(RoutineName name) {
        // TODO: just check for name without loading entire object
        return loadRoutine(name).isPresent();
    }

    @Override
    public void saveRoutine(Routine routine) {
        if(exists(routine.getName())) {
            return;
        }

        try {
            List<RoutineDbEntity> routineDbEntities = objectMapper.readValue(
                    new File(jsonDatabaseConfig.getJson().getRoutineFilepath()),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineDbEntity.class));

            RoutineDbEntity routineToSave = routineJsonMapper.toEntity(routine);
            routineDbEntities.add(routineToSave);
            objectMapper.writeValue(new File(jsonDatabaseConfig.getJson().getRoutineFilepath()), routineDbEntities);
        } catch (IOException e) {
            logger.error("Error adding new routine to database '{}'", routine.getName(), e);
        }
    }
}
