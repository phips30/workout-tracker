package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RoutineRepositoryImpl implements RoutineRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(RoutineRepositoryImpl.class);

    private final JsonDatabaseConfig jsonDatabaseConfig;

    public RoutineRepositoryImpl(JsonDatabaseConfig jsonDatabaseConfig) {
        this.jsonDatabaseConfig = jsonDatabaseConfig;
    }

    @Override
    public Optional<Routine> loadRoutine(String routineName) {
        try {
            List<RoutineDbEntity> routineDbEntities = objectMapper.readValue(
                    new File(jsonDatabaseConfig.getJson().getFilepath()),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineDbEntity.class));

            return routineDbEntities.stream()
                    .filter(routine -> Objects.equals(routine.getName(), routineName))
                    .findFirst()
                    .map(this::convertDbEntityToDomain);
        } catch (IOException e) {
            logger.error("Error parsing the json file for routine name '{}'", routineName, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(String name) {
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
                    new File(jsonDatabaseConfig.getJson().getFilepath()),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineDbEntity.class));

            RoutineDbEntity routineToSave = convertDomainToDbEntity(routine);
            routineDbEntities.add(routineToSave);
            objectMapper.writeValue(new File(jsonDatabaseConfig.getJson().getFilepath()), routineDbEntities);
        } catch (IOException e) {
            logger.error("Error adding new routine to database '{}'", routine.getName(), e);
        }
    }

    private RoutineDbEntity convertDomainToDbEntity(Routine routine) {
        RoutineDbEntity routineToSave = new RoutineDbEntity();
        routineToSave.setName(routine.getName());
        routineToSave.setRoutineType(routine.getName());
        routineToSave.setExercises(routine.getExercises().stream().map(Exercise::getName).toList());
        routineToSave.setRepetitions(routine.getRepetitions().stream().map(Repetition::getNumber).toList());
        return routineToSave;
    }

    private Routine convertDbEntityToDomain(RoutineDbEntity routineDbEntity) {
        return Routine.of(
                routineDbEntity.getName(),
                RoutineType.valueOf(routineDbEntity.getRoutineType()),
                routineDbEntity.getExercises().stream().map(Exercise::of).toList(),
                routineDbEntity.getRepetitions().stream().map(Repetition::of).toList()
        );
    }

    private static class RoutineDbEntity {
        private String name;
        private String routineType;
        private List<String> exercises;
        private List<Integer> repetitions;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoutineType() {
            return routineType;
        }

        public void setRoutineType(String routineType) {
            this.routineType = routineType;
        }

        public List<String> getExercises() {
            return exercises;
        }

        public void setExercises(List<String> exercises) {
            this.exercises = exercises;
        }

        public List<Integer> getRepetitions() {
            return repetitions;
        }

        public void setRepetitions(List<Integer> repetitions) {
            this.repetitions = repetitions;
        }
    }
}
