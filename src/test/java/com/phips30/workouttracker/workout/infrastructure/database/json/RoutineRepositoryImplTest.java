package com.phips30.workouttracker.workout.infrastructure.database.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.database.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private ExerciseRepository exerciseRepository;

    @Mock
    private RoutineJsonMapper routineJsonMapper;

    @Mock
    private ObjectMapper objectMapper;

    private final RoutineDbEntity routineDbEntity = new RoutineDbEntity();
    private Routine routineDomain;
    private final UUID routineId = UUID.randomUUID();
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

        routineDbEntity.setId(UUID.randomUUID());
        routineDbEntity.setName(routineName.getValue());
        routineDbEntity.setRoutineType(routineType.toString());
        routineDbEntity.setExerciseIds(List.of(exerciseId));
        routineDbEntity.setRepetitions(List.of(10));

        routineDomain = Routine.of(
                new EntityId(routineId),
                routineName,
                routineType,
                List.of(new Exercise(new EntityId(exerciseId), new ExerciseName(shortString()))),
                List.of(Repetition.of(10))
        );
    }

    @Test
    void loadRoutine_returnsRoutine_whenRoutineExists() throws Exception {
        // given
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenReturn(List.of(routineDbEntity));

        Exercise exercise = mock(Exercise.class);
        when(exerciseRepository.loadByIds(List.of(new EntityId(exerciseId)))).thenReturn(List.of(exercise));

        when(routineJsonMapper.toDomain(any(RoutineDbEntity.class), anyList()))
                .thenReturn(routineDomain);

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
        when(objectMapper.readValue(any(File.class), any(JavaType.class))).thenReturn(List.of());
        Optional<Routine> result = routineRepository
                .loadRoutine(new RoutineName(shortString()));
        assertTrue(result.isEmpty());
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    void loadRoutine_IOExceptionOccurs_throwsPersistenceException() throws Exception {
        IOException ioException = new IOException();
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenThrow(ioException);

        PersistenceException exception = assertThrows(PersistenceException.class,
                () -> routineRepository.loadRoutine(new RoutineName(shortString())));

        assertSame(ioException, exception.getCause());
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    void loadRoutines_IOExceptionOccurs_throwsPersistenceException() throws Exception {
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenThrow(new IOException());

        assertThrows(PersistenceException.class, () -> routineRepository.loadRoutines());
    }

    @Test
    void exists_IOExceptionOccurs_throwsPersistenceException() throws Exception {
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenThrow(new IOException());

        assertThrows(PersistenceException.class,
                () -> routineRepository.exists(new RoutineName(shortString())));
    }

    @Test
    void saveRoutine_readingFailsWithIOException_throwsPersistenceException() throws Exception {
        when(objectMapper.readValue(any(File.class), any(JavaType.class)))
                .thenThrow(new IOException());

        assertThrows(PersistenceException.class, () -> routineRepository.saveRoutine(routineDomain));
        verify(objectMapper, never()).writeValue(any(File.class), any());
    }

    @Test
    void saveRoutine_writingFailsWithIOException_throwsPersistenceException() throws Exception {
        when(objectMapper.readValue(any(File.class), any(JavaType.class))).thenReturn(new ArrayList<RoutineDbEntity>());
        when(routineJsonMapper.toEntity(routineDomain)).thenReturn(routineDbEntity);
        doThrow(new IOException()).when(objectMapper).writeValue(any(File.class), any());

        assertThrows(PersistenceException.class, () -> routineRepository.saveRoutine(routineDomain));
    }

    @Test
    void saveRoutine_writesRoutineToJson() throws Exception {
        List<RoutineDbEntity> existingEntities = new ArrayList<>();
        when(objectMapper.readValue(any(File.class), any(JavaType.class))).thenReturn(existingEntities);
        when(routineJsonMapper.toEntity(routineDomain)).thenReturn(routineDbEntity);

        routineRepository.saveRoutine(routineDomain);

        verify(objectMapper).writeValue(any(File.class), eq(List.of(routineDbEntity)));
    }
}
