package com.learningplatform.controller;

import com.learningplatform.common.Result;
import com.learningplatform.dto.*;

import com.learningplatform.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exercise")
@Validated
@Slf4j
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    // 获取习题列表
    @GetMapping("/list")
    public Result<ExerciseListResponse> getExerciseList(@Valid ExerciseQueryRequest request) {
        ExerciseListResponse response = exerciseService.getExerciseList(request);
        return Result.success(response);
    }

    // 获取习题详情
    @GetMapping("/{id}")
    public Result<ExerciseDetailResponse> getExerciseDetail(@PathVariable Long id) {
        ExerciseDetailResponse response = exerciseService.getExerciseDetail(id);
        return Result.success(response);
    }

    // 创建习题
    @PostMapping
    public Result<Long> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        Long exerciseId = exerciseService.createExercise(request);
        return Result.success(exerciseId);
    }

    // 更新习题
    @PutMapping("/{id}")
    public Result<Boolean> updateExercise(@PathVariable Long id,
                                               @Valid @RequestBody UpdateExerciseRequest request) {
        request.setId(id);
        Boolean result = exerciseService.updateExercise(request);
        return Result.success(result);
    }

    // 删除习题
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteExercise(@PathVariable Long id) {
        Boolean result = exerciseService.deleteExercise(id);
        return Result.success(result);
    }

    // 提交答案
    @PostMapping("/submit")
    public Result<SubmitResultResponse> submitAnswer(@Valid @RequestBody SubmitAnswerRequest request,
                                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        SubmitResultResponse response = exerciseService.submitAnswer(request, currentUserId);
        return Result.success(response);
    }

    // 获取错题列表
    @GetMapping("/wrong")
    public Result<ExerciseListResponse> getWrongExercises(@Valid ExerciseQueryRequest request,
                                                          @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        ExerciseListResponse response = exerciseService.getWrongExercises(currentUserId, request);
        return Result.success(response);
    }

    // 根据知识点获取习题
    @GetMapping("/knowledge/{knowledgeId}")
    public Result<List<ExerciseResponse>> getExercisesByKnowledge(@PathVariable Long knowledgeId) {
        List<ExerciseResponse> response = exerciseService.getExercisesByKnowledge(knowledgeId);
        return Result.success(response);
    }

    private Long getCurrentUserId(Long userId) {
        if (userId != null) {
            return userId;
        }
        log.warn("未获取到用户ID，使用默认用户1");
        return 1L;
    }
}
