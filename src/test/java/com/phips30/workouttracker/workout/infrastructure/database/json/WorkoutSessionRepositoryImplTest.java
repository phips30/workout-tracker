package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSessionEntry;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkoutSessionRepositoryImplTest {

    @TempDir
    Path tempDir;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final JsonDatabaseConfig jsonDatabaseConfig = new JsonDatabaseConfig();
    private final EntityId routineId = EntityId.generate();
    private final LocalDateTime startedAt = LocalDateTime.of(2025, 1, 1, 10, 0);
    private File dbFile;
    private WorkoutSessionRepositoryImpl repository;

    @BeforeEach
    void setUp() throws IOException {
        dbFile = tempDir.resolve("workout-session-db.json").toFile();
        Files.writeString(dbFile.toPath(), "[]");
        JsonDatabaseConfig.Json json = new JsonDatabaseConfig.Json();
        json.setWorkoutSessionFilepath(dbFile.getPath());
        jsonDatabaseConfig.setJson(json);
        repository = new WorkoutSessionRepositoryImpl(objectMapper, jsonDatabaseConfig, new WorkoutSessionJsonMapper());
    }

    private WorkoutSession session(EntityId routineId) {
        return WorkoutSession.of(EntityId.generate(), routineId, startedAt, Map.of("weight", 10), List.of(
                WorkoutSessionEntry.ofRepetitions(1, 1, 1, 8),
                WorkoutSessionEntry.ofSeconds(1, 1, 2, true)));
    }

    @Test
    void saveAndLoad_roundTripsSession() {
        WorkoutSession saved = repository.save(session(routineId));

        List<WorkoutSession> loaded = repository.loadForRoutine(routineId);

        assertEquals(1, loaded.size());
        WorkoutSession result = loaded.getFirst();
        assertEquals(saved.getId(), result.getId());
        assertEquals(routineId, result.getRoutineId());
        assertEquals(startedAt, result.getStartedAt());
        assertEquals(Map.of("weight", 10), result.getMetadata());
        assertEquals(saved.getEntries(), result.getEntries());
    }

    @Test
    void loadForRoutine_returnsOnlySessionsOfThatRoutine() {
        repository.save(session(routineId));
        repository.save(session(EntityId.generate()));

        assertEquals(1, repository.loadForRoutine(routineId).size());
    }

    @Test
    void loadForRoutine_emptyFile_returnsNothing() throws IOException {
        Files.writeString(dbFile.toPath(), "");

        assertTrue(repository.loadForRoutine(routineId).isEmpty());
    }

    @Test
    void loadForRoutine_unreadableFile_throwsPersistenceException() throws IOException {
        Files.writeString(dbFile.toPath(), "not json");

        PersistenceException exception =
                assertThrows(PersistenceException.class, () -> repository.loadForRoutine(routineId));
        assertNotNull(exception.getCause());
    }

    @Test
    void save_writingFails_throwsPersistenceException() throws IOException {
        ObjectMapper failingMapper = mock(ObjectMapper.class);
        when(failingMapper.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());
        when(failingMapper.readValue(any(File.class), any(JavaType.class))).thenReturn(new java.util.ArrayList<>());
        IOException ioException = new IOException();
        doThrow(ioException).when(failingMapper).writeValue(any(File.class), any());
        WorkoutSessionRepositoryImpl failingRepository =
                new WorkoutSessionRepositoryImpl(failingMapper, jsonDatabaseConfig, new WorkoutSessionJsonMapper());

        PersistenceException exception =
                assertThrows(PersistenceException.class, () -> failingRepository.save(session(routineId)));
        assertSame(ioException, exception.getCause());
    }
}
