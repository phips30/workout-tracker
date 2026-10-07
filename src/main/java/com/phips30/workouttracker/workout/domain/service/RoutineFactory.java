package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates new routines and guarantees that routine names are unique
 * and that every referenced exercise exists
 */
public class RoutineFactory {

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    public RoutineFactory(RoutineRepository routineRepository, ExerciseRepository exerciseRepository) {
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public Routine of(RoutineName name,
                      RoutineType routineType,
                      List<EntityId> exerciseIds,
                      List<Repetition> repetitions) throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        if (routineRepository.exists(name)) {
            throw new RoutineAlreadyExistsException(name);
        }

        return Routine.createNew(name, routineType, loadExercises(exerciseIds), repetitions);
    }

    /**
     * Returns the exercises in the order of the given ids, so they line up with the repetitions
     */
    private List<Exercise> loadExercises(List<EntityId> exerciseIds) throws ExerciseNotFoundException {
        Map<EntityId, Exercise> exercisesById = new HashMap<>();
        for (Exercise exercise : exerciseRepository.loadByIds(exerciseIds)) {
            exercisesById.put(exercise.getId(), exercise);
        }

        for (EntityId exerciseId : exerciseIds) {
            if (!exercisesById.containsKey(exerciseId)) {
                throw new ExerciseNotFoundException(exerciseId);
            }
        }
        return exerciseIds.stream().map(exercisesById::get).toList();
    }
}
