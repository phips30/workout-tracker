package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import org.junit.jupiter.api.Assertions;
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

import static com.phips30.workouttracker.RandomData.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineRepositoryImplTest {


    @InjectMocks
    private RoutineRepositoryImpl routineRepository;

    private final String routineName = shortString();
    private final RoutineType routineType = RoutineType.AMRAP;
    private final List<Exercise> exercises = List.of(
            Exercise.of("Burpees"),
            Exercise.of("Mountain climbers"),
            Exercise.of("4-Count burpees"),
            Exercise.of("Jumping jacks")
    );
    private final List<Repetition> repetitions = List.of(
            Repetition.of(positiveDigit()),
            Repetition.of(positiveDigit()),
            Repetition.of(positiveDigit()),
            Repetition.of(positiveDigit())
    );

    @Mock
    private JsonDatabaseConfig jsonDatabaseConfig;

    @Mock
    private JsonDatabaseConfig.Json json;

    @TempDir
    File tempFolder;

    File tempFile = new File(tempFolder, "workout-test-db.json");


    @BeforeEach
    void setUp() {
        when(jsonDatabaseConfig.getJson()).thenReturn(json);
        when(json.getFilepath()).thenReturn(tempFile.getAbsolutePath());
    }

    @Test
    void loadRoutine_noRoutinesInJson_returnsNothing() throws IOException {
        String jsonContent = "[]";
        Files.write(tempFile.toPath(), jsonContent.getBytes());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        Assertions.assertFalse(routineFromDb.isPresent());
    }

    @Test
    void loadRoutine_existsInJsonAsSingleRoutine_returnsRoutine() throws IOException {
        String jsonContent = String.format("[{" +
                "  \"name\": \"%s\"," +
                "  \"routineType\": \"AMRAP\"," +
                "  \"exercises\": [\"burpees\", \"mountain climbers\", \"burpees with legs out jump\", \"jumping jacks\"]," +
                "  \"repetitions\": [10, 40, 10, 10]" +
                "}]", routineName);
        Files.write(tempFile.toPath(), jsonContent.getBytes());

        Optional<Routine> routineFromDb = routineRepository.loadRoutine(routineName);
        Assertions.assertTrue(routineFromDb.isPresent());
    }

    @Test
    void exists() {
    }

    @Test
    void saveRoutine() {
        Routine routineToSave = Routine.of(
                routineName,
                routineType,
                exercises,
                repetitions
        );

        routineRepository.saveRoutine(routineToSave);
    }
}