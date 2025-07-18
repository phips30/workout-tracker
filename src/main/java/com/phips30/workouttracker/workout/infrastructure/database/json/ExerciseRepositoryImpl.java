package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class ExerciseRepositoryImpl implements ExerciseRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(ExerciseRepositoryImpl.class);

    private final JsonDatabaseConfig jsonDatabaseConfig;
    private final File exerciseDbFile;

    public ExerciseRepositoryImpl(JsonDatabaseConfig jsonDatabaseConfig) {
        this.jsonDatabaseConfig = jsonDatabaseConfig;
        this.exerciseDbFile = new File(jsonDatabaseConfig.getJson().getExerciseFilepath());
    }

    @Override
    public boolean exists(String exerciseName) {
        try {
            if (exerciseDbFile.length() == 0) {
                return false;
            }

            List<ExerciseDbEntity> exerciseDbEntities = objectMapper.readValue(
                    exerciseDbFile,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ExerciseDbEntity.class));

            return exerciseDbEntities.stream()
                    .anyMatch(exercise -> Objects.equals(exercise.getName(), exerciseName));
        } catch (IOException e) {
            logger.error("Error parsing the json file for exercise name '{}'", exerciseName, e);
        }
        return false;
    }

    @Override
    public Optional<Exercise> create(Exercise exercise) {
        // Todo: Rewrite to return a result object instead of an optional
        try {
            List<ExerciseDbEntity> exerciseDbEntities = new ArrayList<>();

            if (exerciseDbFile.length() > 0) {
                exerciseDbEntities = objectMapper.readValue(
                        new File(jsonDatabaseConfig.getJson().getRoutineFilepath()),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ExerciseDbEntity.class));
            }

            ExerciseDbEntity exerciseToSave = convertDomainToDbEntity(exercise);
            exerciseDbEntities.add(exerciseToSave);
            objectMapper.writeValue(exerciseDbFile, exerciseDbEntities);
            return Optional.of(exercise);
        } catch (IOException e) {
            logger.error("Error adding new exercise to database '{}'", exercise.getName(), e);
        }
        return Optional.empty();
    }

    private ExerciseDbEntity convertDomainToDbEntity(Exercise exercise) {
        ExerciseDbEntity exerciseToSave = new ExerciseDbEntity();
        exerciseToSave.setId(UUID.randomUUID());
        exerciseToSave.setName(exercise.getName());
        return exerciseToSave;
    }

    @Getter
    @Setter
    private static class ExerciseDbEntity {
        private UUID id;
        private String name;
    }
}
