package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ExerciseRepositoryImpl implements ExerciseRepository {

    private final ObjectMapper objectMapper;

    private final File exerciseDbFile;

    public ExerciseRepositoryImpl(ObjectMapper objectMapper,
                                  JsonDatabaseConfig jsonDatabaseConfig) {
        this.objectMapper = objectMapper;
        this.exerciseDbFile = new File(jsonDatabaseConfig.getJson().getExerciseFilepath());
    }

    @Override
    public boolean exists(ExerciseName exerciseName) {
        try {
            return readExercises().stream()
                    .anyMatch(exercise -> Objects.equals(exercise.getName(), exerciseName.getValue()));
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to read exercises while checking for exercise '%s'", exerciseName), e);
        }
    }

    @Override
    public Exercise save(Exercise exercise) {
        try {
            List<ExerciseDbEntity> exerciseDbEntities = readExercises();
            exerciseDbEntities.add(convertDomainToDbEntity(exercise));
            objectMapper.writeValue(exerciseDbFile, exerciseDbEntities);
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to save exercise '%s'", exercise.getName()), e);
        }
        return exercise;
    }

    @Override
    public List<Exercise> loadAll() {
        try {
            return readExercises().stream()
                    .map(this::convertDbEntityToDomain)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenceException("Failed to load exercises", e);
        }
    }

    @Override
    public List<Exercise> loadByIds(List<EntityId> exerciseIds) {
        List<UUID> ids = exerciseIds.stream().map(EntityId::getId).toList();
        try {
            return readExercises().stream()
                    .filter(e -> ids.contains(e.id))
                    .map(this::convertDbEntityToDomain)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenceException("Failed to load exercises by id", e);
        }
    }

    /**
     * Reads all stored exercises. A completely empty file is treated as an empty database.
     */
    private List<ExerciseDbEntity> readExercises() throws IOException {
        if (exerciseDbFile.exists() && exerciseDbFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(
                exerciseDbFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ExerciseDbEntity.class));
    }

    private Exercise convertDbEntityToDomain(ExerciseDbEntity exerciseDbEntity) {
        return new Exercise(
                new EntityId(exerciseDbEntity.id),
                new ExerciseName(exerciseDbEntity.name)
        );
    }

    private ExerciseDbEntity convertDomainToDbEntity(Exercise exercise) {
        ExerciseDbEntity exerciseToSave = new ExerciseDbEntity();
        exerciseToSave.setId(exercise.getId().getId());
        exerciseToSave.setName(exercise.getName().getValue());
        return exerciseToSave;
    }

    private static class ExerciseDbEntity {
        private UUID id;
        private String name;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
