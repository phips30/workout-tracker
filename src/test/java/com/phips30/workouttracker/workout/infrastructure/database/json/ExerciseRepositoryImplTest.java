package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.phips30.workouttracker.RandomData.shortString;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseRepositoryImplTest {

    @TempDir
    Path tempDir;

    private Path exerciseFile;
    private ExerciseRepositoryImpl exerciseRepository;

    @BeforeEach
    void setUp() throws IOException {
        exerciseFile = tempDir.resolve("exercise-db.json");
        Files.writeString(exerciseFile, "[]");
        exerciseRepository = createRepository(exerciseFile);
    }

    private ExerciseRepositoryImpl createRepository(Path file) {
        JsonDatabaseConfig.Json json = new JsonDatabaseConfig.Json();
        json.setExerciseFilepath(file.toString());
        JsonDatabaseConfig config = new JsonDatabaseConfig();
        config.setJson(json);
        return new ExerciseRepositoryImpl(new ObjectMapper(), config);
    }

    @Test
    void save_thenLoadAll_returnsSavedExercises() {
        Exercise first = new Exercise(new ExerciseName(shortString()));
        Exercise second = new Exercise(new ExerciseName(shortString()));

        exerciseRepository.save(first);
        exerciseRepository.save(second);

        assertEquals(List.of(first, second), exerciseRepository.loadAll());
    }

    @Test
    void save_emptyFile_treatedAsEmptyDatabase() throws IOException {
        Files.writeString(exerciseFile, "");
        Exercise exercise = new Exercise(new ExerciseName(shortString()));

        exerciseRepository.save(exercise);

        assertEquals(List.of(exercise), exerciseRepository.loadAll());
    }

    @Test
    void loadAll_emptyFile_returnsNoExercises() throws IOException {
        Files.writeString(exerciseFile, "");

        assertTrue(exerciseRepository.loadAll().isEmpty());
    }

    @Test
    void exists_savedName_returnsTrue() {
        Exercise exercise = new Exercise(new ExerciseName(shortString()));
        exerciseRepository.save(exercise);

        assertTrue(exerciseRepository.exists(exercise.getName()));
        assertFalse(exerciseRepository.exists(new ExerciseName(shortString() + "x")));
    }

    @Test
    void loadByIds_returnsOnlyRequestedExercises() {
        Exercise first = new Exercise(new ExerciseName(shortString()));
        Exercise second = new Exercise(new ExerciseName(shortString()));
        exerciseRepository.save(first);
        exerciseRepository.save(second);

        List<Exercise> result = exerciseRepository.loadByIds(List.of(second.getId(), EntityId.generate()));

        assertEquals(List.of(second), result);
    }

    @Test
    void loadAll_corruptJson_throwsPersistenceException() throws IOException {
        Files.writeString(exerciseFile, "{ this is not valid json");

        assertThrows(PersistenceException.class, () -> exerciseRepository.loadAll());
    }

    @Test
    void loadByIds_corruptJson_throwsPersistenceException() throws IOException {
        Files.writeString(exerciseFile, "{ this is not valid json");

        assertThrows(PersistenceException.class,
                () -> exerciseRepository.loadByIds(List.of(EntityId.generate())));
    }

    @Test
    void exists_corruptJson_throwsPersistenceException() throws IOException {
        Files.writeString(exerciseFile, "{ this is not valid json");

        assertThrows(PersistenceException.class,
                () -> exerciseRepository.exists(new ExerciseName(shortString())));
    }

    @Test
    void save_corruptJson_throwsPersistenceExceptionAndKeepsFileUntouched() throws IOException {
        String corrupt = "{ this is not valid json";
        Files.writeString(exerciseFile, corrupt);

        assertThrows(PersistenceException.class,
                () -> exerciseRepository.save(new Exercise(new ExerciseName(shortString()))));
        assertEquals(corrupt, Files.readString(exerciseFile));
    }

    @Test
    void save_fileCannotBeWritten_throwsPersistenceException() {
        // a directory can neither be read nor written as a json file
        ExerciseRepositoryImpl repository = createRepository(tempDir);

        PersistenceException exception = assertThrows(PersistenceException.class,
                () -> repository.save(new Exercise(new ExerciseName(shortString()))));

        assertNotNull(exception.getCause());
    }

    @Test
    void loadAll_fileDoesNotExist_throwsPersistenceException() {
        ExerciseRepositoryImpl repository = createRepository(tempDir.resolve("missing.json"));

        assertThrows(PersistenceException.class, repository::loadAll);
    }
}
