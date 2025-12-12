package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
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

    private final RoutineName routineName = new RoutineName(shortString());
    private final RoutineType routineType = RoutineType.AMRAP;
    private final List<Exercise> exercisesForRoutineToLookup = List.of(
            new Exercise(new ExerciseName(shortString())),
            new Exercise(new ExerciseName(shortString()))
    );
    private final List<Repetition> repetitions = List.of(
            Repetition.of(positiveDigit()),
            Repetition.of(positiveDigit())
    );

    private final String routineToLookup = String.format("{" +
            " \"id\": \"" + randomUUID() + "\"," +
            "  \"name\": \"%s\"," +
            "  \"routineType\": \"AMRAP\"," +
            "  \"exerciseIds\": [%s]," +
            "  \"repetitions\": [%s]" +
            "}",
            routineName,
            exercisesForRoutineToLookup.stream().map(e -> "\"" + e.getId() + "\"").collect(Collectors.joining(", ")),
            repetitions.stream().map(r -> String.valueOf(r.getNumber())).collect(Collectors.joining(", ")));

    private final List<Exercise> exercisesForDummyRoutine = List.of(
            new Exercise(new ExerciseName(shortString())),
            new Exercise(new ExerciseName(shortString()))
    );

    private final String dummyRoutine = String.format("{" +
            " \"id\": \"" + randomUUID() + "\"," +
            " \"name\": \"OtherRoutine\"," +
            " \"routineType\": \"AMRAP\"," +
            " \"exerciseIds\": [%s]," +
            " \"repetitions\": [10, 40, 10, 10]" +
            " }",
            exercisesForDummyRoutine.stream().map(e -> "\"" + e.getId() + "\"").collect(Collectors.joining(", "))
    );

    @Mock
    private JsonDatabaseConfig jsonDatabaseConfig;

    @Mock
    private JsonDatabaseConfig.Json json;

    @Mock
    private ExerciseRepositoryImpl exerciseRepository;

    @TempDir
    File tempFolder;

    File emptyWorkoutDbFile = new File(tempFolder, "workout-test-empty-db.json");
    File multipleWorkoutDbFile = new File(tempFolder, "workout-test-multiple-db.json");

    @BeforeEach
    void setUp() throws IOException {
        when(jsonDatabaseConfig.getJson()).thenReturn(json);

        String emptyWorkoutDb = "[]";
        Files.write(emptyWorkoutDbFile.toPath(), emptyWorkoutDb.getBytes());

        String multipleWorkoutDb =  "[" +
                dummyRoutine + ", " +
                routineToLookup +
                "]";
        Files.write(multipleWorkoutDbFile.toPath(), multipleWorkoutDb.getBytes());
    }

    @Test
    void loadRoutine_noRoutinesInJson_returnsNothing() {
        when(json.getRoutineFilepath()).thenReturn(emptyWorkoutDbFile.getAbsolutePath());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        assertFalse(routineFromDb.isPresent());
    }

    @Test
    void loadRoutine_existsInJson_returnsRoutine() {
        when(json.getRoutineFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());
        when(exerciseRepository.loadByIds(
                exercisesForRoutineToLookup.stream().map(e -> e.getId().getId()).collect(Collectors.toList())
        )).thenReturn(exercisesForRoutineToLookup);

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        assertTrue(routineFromDb.isPresent());
        assertEquals(routineName, routineFromDb.get().getName());
        assertEquals(
                exercisesForRoutineToLookup.stream().map(Exercise::getName).toList(),
                routineFromDb.get().getExercises().stream().map(Exercise::getName).toList());
        assertEquals(
                repetitions.stream().map(Repetition::getNumber).toList(),
                routineFromDb.get().getRepetitions().stream().map(Repetition::getNumber).toList());
    }

    @Test
    void exists_routineDoesNotExist_returnsFalse() {
        when(json.getRoutineFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());
        assertFalse(routineRepository.exists(new RoutineName(shortString())));
    }

    @Test
    void exists_routineDoesExist_returnsTrue() {
        when(json.getRoutineFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());
        when(exerciseRepository.loadByIds(
                exercisesForRoutineToLookup.stream().map(e -> e.getId().getId()).collect(Collectors.toList())
        )).thenReturn(exercisesForRoutineToLookup);
        assertTrue(routineRepository.exists(routineName));
    }

    @Test
    void saveRoutine_routineDoesNotYetExist_savesDb() {
        when(json.getRoutineFilepath()).thenReturn(multipleWorkoutDbFile.getAbsolutePath());

        Routine routineToSave = Routine.of(
                EntityId.generate(),
                new RoutineName(randomString(5)),
                routineType,
                exercisesForRoutineToLookup,
                repetitions
        );

        routineRepository.saveRoutine(routineToSave);
    }
}