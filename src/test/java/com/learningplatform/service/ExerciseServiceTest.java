package com.learningplatform.service;

import com.learningplatform.entity.Exercise;
import com.learningplatform.repository.ExerciseRepository;
import com.learningplatform.repository.KnowledgePointRepository;
import com.learningplatform.repository.UserExerciseRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserExerciseRecordRepository userExerciseRecordRepository;

    @Mock
    private KnowledgePointRepository knowledgePointRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    void shouldCallDeleteByIdForLogicalDelete() {
        Exercise exercise = new Exercise();
        exercise.setId(1L);
        when(exerciseRepository.selectById(1L)).thenReturn(exercise);

        exerciseService.deleteExercise(1L);

        verify(exerciseRepository).deleteById(1L);
        verify(exerciseRepository, never()).updateById(any(Exercise.class));
    }
}
