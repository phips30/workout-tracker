package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.phips30.workouttracker.RandomData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineRepositoryImplTest {

    @InjectMocks
    private RoutineRepositoryImpl routineRepository;

    private final String routineName = shortString();
    private final RoutineType routineType = RoutineType.AMRAP;
    private final List<Exercise> exercises = List.of(
            Exercise.of(shortString()),
            Exercise.of(shortString())
    );
    private final List<Repetition> repetitions = List.of(
            Repetition.of(positiveDigit()),
            Repetition.of(positiveDigit())
    );

    private final String routineToLookup = String.format("{" +
            "  \"name\": \"%s\"," +
            "  \"routineType\": \"AMRAP\"," +
            "  \"exercises\": [%s]," +
            "  \"repetitions\": [%s]" +
            "}",
            routineName,
            exercises.stream().map(e -> "\"" + e.getName() + "\"").collect(Collectors.joining(", ")),
            repetitions.stream().map(r -> String.valueOf(r.getNumber())).collect(Collectors.joining(", ")));

    private final String dummyRoutine = "{" +
            " \"name\": \"OtherRoutine\"," +
            " \"routineType\": \"AMRAP\"," +
            " \"exercises\": [\"burpees\", \"mountain climbers\", \"burpees with legs out jump\", \"jumping jacks\"]," +
            " \"repetitions\": [10, 40, 10, 10]" +
            " }";

    @Mock
    private JsonDatabaseConfig jsonDatabaseConfig;

    @Mock
    private JsonDatabaseConfig.Json json;

    @TempDir
    File tempFolder;

    File emptyWorkoutDbFile = new File(tempFolder, "workout-test-empty-db.json");
    File singleWorkoutDbFile = new File(tempFolder, "workout-test-single-db.json");
    File multipleWorkoutDbFile = new File(tempFolder, "workout-test-multiple-db.json");

    @BeforeEach
    void setUp() throws IOException {
        when(jsonDatabaseConfig.getJson()).thenReturn(json);

        String emptyWorkoutDb = "[]";
        Files.write(emptyWorkoutDbFile.toPath(), emptyWorkoutDb.getBytes());

        String singleWorkoutDb = "[" + routineToLookup + "]";
        Files.write(singleWorkoutDbFile.toPath(), singleWorkoutDb.getBytes());

        String multipleWorkoutDb =  "[" +
                dummyRoutine + ", " +
                routineToLookup +
                "]";
        Files.write(multipleWorkoutDbFile.toPath(), multipleWorkoutDb.getBytes());
    }

    @Test
    void loadRoutine_noRoutinesInJson_returnsNothing() {
        when(json.getFilepath()).thenReturn(emptyWorkoutDbFile.getAbsolutePath());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        assertFalse(routineFromDb.isPresent());
    }

    @Test
    void loadRoutine_existsInJsonAsSingleRoutine_returnsRoutine() {
        when(json.getFilepath()).thenReturn(singleWorkoutDbFile.getAbsolutePath());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        assertTrue(routineFromDb.isPresent());
        assertEquals(routineName, routineFromDb.get().getName());
        assertEquals(
                exercises.stream().map(Exercise::getName).toList(),
                routineFromDb.get().getExercises().stream().map(Exercise::getName).toList());
        assertEquals(
                repetitions.stream().map(Repetition::getNumber).toList(),
                routineFromDb.get().getRepetitions().stream().map(Repetition::getNumber).toList());
    }

    @Test
    void loadRoutine_existsInJsonWithMultipleRoutines_returnsRoutine() {
        when(json.getFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        assertTrue(routineFromDb.isPresent());
        assertEquals(routineName, routineFromDb.get().getName());
        assertEquals(
                exercises.stream().map(Exercise::getName).toList(),
                routineFromDb.get().getExercises().stream().map(Exercise::getName).toList());
        assertEquals(
                repetitions.stream().map(Repetition::getNumber).toList(),
                routineFromDb.get().getRepetitions().stream().map(Repetition::getNumber).toList());
    }

    @Test
    void exists_routineDoesNotExist_returnsFalse() {
        when(json.getFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());
        assertFalse(routineRepository.exists(shortString()));
    }

    @Test
    void exists_routineDoesExist_returnsTrue() {
        when(json.getFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());
        assertTrue(routineRepository.exists(routineName));
    }

    @Test
    void saveRoutine_routineDoesNotYetExist_savesDb() {
        when(json.getFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());

        Routine routineToSave = Routine.of(
                "a new routine",
                routineType,
                exercises,
                repetitions
        );

        routineRepository.saveRoutine(routineToSave);
    }
}