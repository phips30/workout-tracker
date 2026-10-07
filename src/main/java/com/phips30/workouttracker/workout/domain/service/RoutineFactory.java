package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
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

    /**
     * Describes a block to create; its items reference their exercise by id
     */
    public record BlockDefinition(int position, int rounds, List<ItemDefinition> items) {
    }

    public record ItemDefinition(int position, EntityId exerciseId, Repetition repetition) {
    }

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    public RoutineFactory(RoutineRepository routineRepository, ExerciseRepository exerciseRepository) {
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public Routine of(RoutineName name,
                      RoutineType routineType,
                      List<BlockDefinition> blockDefinitions) throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        if (routineRepository.exists(name)) {
            throw new RoutineAlreadyExistsException(name);
        }
        AssertionHelper.assertNotNullOrEmpty(blockDefinitions, "Blocks is null or empty");

        Map<EntityId, Exercise> exercisesById = loadExercises(blockDefinitions);
        List<RoutineBlock> blocks = blockDefinitions.stream()
                .map(block -> toBlock(block, exercisesById))
                .toList();
        return Routine.createNew(name, routineType, blocks);
    }

    private RoutineBlock toBlock(BlockDefinition block, Map<EntityId, Exercise> exercisesById) {
        List<RoutineBlockItem> items = block.items().stream()
                .map(item -> RoutineBlockItem.of(item.position(), exercisesById.get(item.exerciseId()), item.repetition()))
                .toList();
        return RoutineBlock.of(block.position(), block.rounds(), items);
    }

    /**
     * Loads every referenced exercise once and fails if one of them does not exist
     */
    private Map<EntityId, Exercise> loadExercises(List<BlockDefinition> blockDefinitions) throws ExerciseNotFoundException {
        List<EntityId> exerciseIds = blockDefinitions.stream()
                .peek(block -> AssertionHelper.assertNotNullOrEmpty(block.items(), "Items is null or empty"))
                .flatMap(block -> block.items().stream())
                .map(ItemDefinition::exerciseId)
                .distinct()
                .toList();

        Map<EntityId, Exercise> exercisesById = new HashMap<>();
        for (Exercise exercise : exerciseRepository.loadByIds(exerciseIds)) {
            exercisesById.put(exercise.getId(), exercise);
        }

        for (EntityId exerciseId : exerciseIds) {
            if (!exercisesById.containsKey(exerciseId)) {
                throw new ExerciseNotFoundException(exerciseId);
            }
        }
        return exercisesById;
    }
}
