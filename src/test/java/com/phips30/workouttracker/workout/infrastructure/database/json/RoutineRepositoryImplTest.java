package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.phips30.workouttracker.RandomData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutineRepositoryImplTest {

    @InjectMocks
    private RoutineRepositoryImpl routineRepository;

    @Mock
    private JsonDatabaseConfig jsonDatabaseConfig;

    @Mock
    private ExerciseRepositoryImpl exerciseRepository;

    @Mock
    private ObjectMapper objectMapper;

    private final RoutineDbEntity routine = new RoutineDbEntity();
    private final RoutineName routineName = new RoutineName(shortString());
    private final RoutineType routineType = RoutineType.AMRAP;
    private final UUID exerciseId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Setup mocks
        JsonDatabaseConfig.Json json = mock(JsonDatabaseConfig.Json.class);
        when(jsonDatabaseConfig.getJson()).thenReturn(json);
        when(json.getRoutineFilepath()).thenReturn("path");

        when(objectMapper.getTypeFactory())
                .thenReturn(TypeFactory.defaultInstance());

        routine.setId(UUID.randomUUID());
        routine.setName(routineName.getValue());
        routine.setRoutineType(routineType.toString());
        routine.setExerciseIds(List.of(exerciseId));
        routine.setRepetitions(List.of(10));
    }

    @Test
    void loadRoutine_returnsRoutine_whenRoutineExists() throws Exception {
        // given
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenReturn(Set.of(routine));

        Exercise exercise = mock(Exercise.class);
        when(exerciseRepository.loadByIds(List.of(exerciseId))).thenReturn(List.of(exercise));

        // when
        Optional<Routine> result = routineRepository.loadRoutine(routineName);

        // then
        assertTrue(result.isPresent());

        Routine routine = result.get();
        assertEquals(routineName, routine.getName());
        assertEquals(routineType, routine.getRoutineType());
        assertEquals(1, routine.getExercises().size());
        assertEquals(1, routine.getRepetitions().size());
    }

    @Test
    void loadRoutine_noRoutinesInJson_returnsNothing() throws IOException {
        when(objectMapper.readValue(any(File.class), any(JavaType.class))).thenReturn(Set.of());
        Optional<Routine> result = routineRepository
                .loadRoutine(new RoutineName(shortString()));
        assertTrue(result.isEmpty());
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    void loadRoutine_IOExceptionOccurs_returnsEmptyOptional() throws Exception {
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenThrow(new IOException());
        Optional<Routine> result =
                routineRepository.loadRoutine(new RoutineName(shortString()));
        assertTrue(result.isEmpty());
        verifyNoInteractions(exerciseRepository);
    }
}