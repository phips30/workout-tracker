package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
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

    public RoutineRepositoryImpl(ObjectMapper objectMapper,
                                 JsonDatabaseConfig jsonDatabaseConfig,
                                 ExerciseRepositoryImpl exerciseRepository) {
        this.objectMapper = objectMapper;
        this.jsonDatabaseConfig = jsonDatabaseConfig;
        this.exerciseRepository = exerciseRepository;
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
                        return convertDbEntityToDomain(r, e);
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
                        return convertDbEntityToDomain(r, e);
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

            RoutineDbEntity routineToSave = convertDomainToDbEntity(routine);
            routineDbEntities.add(routineToSave);
            objectMapper.writeValue(new File(jsonDatabaseConfig.getJson().getRoutineFilepath()), routineDbEntities);
        } catch (IOException e) {
            logger.error("Error adding new routine to database '{}'", routine.getName(), e);
        }
    }

    private RoutineDbEntity convertDomainToDbEntity(Routine routine) {
        RoutineDbEntity routineToSave = new RoutineDbEntity();
        routineToSave.setId(routine.getId().getId());
        routineToSave.setName(routine.getName().getValue());
        routineToSave.setRoutineType(routine.getRoutineType().toString());
        routineToSave.setExerciseIds(routine.getExercises().stream().map(Exercise::getId).map(EntityId::getId).toList());
        routineToSave.setRepetitions(routine.getRepetitions().stream().map(Repetition::getNumber).toList());
        return routineToSave;
    }

    private Routine convertDbEntityToDomain(RoutineDbEntity routineDbEntity, List<Exercise> exercises) {
        return Routine.of(
                new EntityId(routineDbEntity.getId()),
                new RoutineName(routineDbEntity.getName()),
                RoutineType.valueOf(routineDbEntity.getRoutineType()),
                exercises,
                routineDbEntity.getRepetitions().stream().map(Repetition::of).toList()
        );
    }
}
