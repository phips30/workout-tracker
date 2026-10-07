package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.repository.WorkoutSessionRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkoutSessionRepositoryImpl implements WorkoutSessionRepository {

    private final ObjectMapper objectMapper;
    private final JsonDatabaseConfig jsonDatabaseConfig;
    private final WorkoutSessionJsonMapper workoutSessionJsonMapper;

    public WorkoutSessionRepositoryImpl(ObjectMapper objectMapper,
                                        JsonDatabaseConfig jsonDatabaseConfig,
                                        WorkoutSessionJsonMapper workoutSessionJsonMapper) {
        this.objectMapper = objectMapper;
        this.jsonDatabaseConfig = jsonDatabaseConfig;
        this.workoutSessionJsonMapper = workoutSessionJsonMapper;
    }

    @Override
    public List<WorkoutSession> loadForRoutine(EntityId routineId) {
        try {
            return readWorkoutSessions().stream()
                    .filter(session -> routineId.getId().equals(session.getRoutineId()))
                    .map(workoutSessionJsonMapper::toDomain)
                    .toList();
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to load workout sessions of routine '%s'", routineId), e);
        }
    }

    @Override
    public WorkoutSession save(WorkoutSession workoutSession) {
        try {
            List<WorkoutSessionDbEntity> entities = readWorkoutSessions();
            entities.add(workoutSessionJsonMapper.toEntity(workoutSession));
            objectMapper.writeValue(workoutSessionDbFile(), entities);
        } catch (IOException e) {
            throw new PersistenceException(
                    String.format("Failed to save workout session '%s'", workoutSession.getId()), e);
        }
        return workoutSession;
    }

    private File workoutSessionDbFile() {
        return new File(jsonDatabaseConfig.getJson().getWorkoutSessionFilepath());
    }

    /**
     * Reads all stored workout sessions. A completely empty file is treated as an empty database.
     */
    private List<WorkoutSessionDbEntity> readWorkoutSessions() throws IOException {
        File dbFile = workoutSessionDbFile();
        if (dbFile.exists() && dbFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(
                dbFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, WorkoutSessionDbEntity.class));
    }
}
