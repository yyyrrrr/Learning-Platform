package com.learningplatform.controller;

import com.learningplatform.common.Result;
import com.learningplatform.dto.StatisticsDTO.*;
import com.learningplatform.entity.LearningProgress;
import com.learningplatform.entity.LearningRecord;
import com.learningplatform.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@Validated
@Slf4j
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取完整统计数据
     */
    @GetMapping("/overview")
    public Result<StatisticsVO> getStatistics(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        StatisticsVO statistics = statisticsService.getStatistics(currentUserId);
        return Result.success(statistics);
    }

    /**
     * 获取学习概况
     */
    @GetMapping("/overview/detail")
    public Result<OverviewVO> getOverviewDetail(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        OverviewVO overview = statisticsService.getOverview(currentUserId);
        return Result.success(overview);
    }

    /**
     * 获取学习进度分析
     */
    @GetMapping("/progress")
    public Result<List<ModuleProgressVO>> getProgressAnalysis(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        List<ModuleProgressVO> progress = statisticsService.getProgressAnalysis(currentUserId);
        return Result.success(progress);
    }

    /**
     * 获取掌握度分析
     */
    @GetMapping("/mastery")
    public Result<List<ModuleMasteryVO>> getMasteryAnalysis(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        List<ModuleMasteryVO> mastery = statisticsService.getMasteryAnalysis(currentUserId);
        return Result.success(mastery);
    }

    /**
     * 获取学习趋势
     */
    @GetMapping("/trend")
    public Result<List<TrendDataVO>> getTrendData(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(defaultValue = "7") int days) {
        Long currentUserId = getCurrentUserId(userId);
        List<TrendDataVO> trend = statisticsService.getTrendData(currentUserId, days);
        return Result.success(trend);
    }

    /**
     * 获取薄弱知识点
     */
    @GetMapping("/weak-points")
    public Result<List<WeakPointVO>> getWeakPoints(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        Long currentUserId = getCurrentUserId(userId);
        List<WeakPointVO> weakPoints = statisticsService.getWeakPoints(currentUserId, limit);
        return Result.success(weakPoints);
    }

    /**
     * 获取错题列表
     */
    @GetMapping("/wrong-questions")
    public Result<List<WrongQuestionVO>> getWrongQuestions(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(required = false) String module,
            @RequestParam(defaultValue = "20") int limit) {
        Long currentUserId = getCurrentUserId(userId);
        List<WrongQuestionVO> wrongQuestions = statisticsService.getWrongQuestions(currentUserId, module, limit);
        return Result.success(wrongQuestions);
    }

    /**
     * 记录学习行为
     */
    @PostMapping("/record")
    public Result<Boolean> recordLearningBehavior(
            @RequestBody LearningRecord record,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        record.setUserId(currentUserId);
        record.setCreateTime(LocalDateTime.now());

        boolean result = statisticsService.recordLearningBehavior(record);
        return result ? Result.success(true) : Result.error("记录失败");
    }

    /**
     * 更新学习进度
     */
    @PutMapping("/progress")
    public Result<Boolean> updateLearningProgress(
            @RequestBody LearningProgress progress,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        progress.setUserId(currentUserId);

        boolean result = statisticsService.updateLearningProgress(progress);
        return result ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 获取当前用户的学习进度
     */
    @GetMapping("/progress/all")
    public Result<List<LearningProgress>> getLearningProgress(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        List<LearningProgress> progressList = statisticsService.getLearningProgress(currentUserId);
        return Result.success(progressList);
    }

    /**
     * 获取指定知识点的进度
     */
    @GetMapping("/progress/{knowledgePointId}")
    public Result<LearningProgress> getKnowledgePointProgress(
            @PathVariable Long knowledgePointId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        LearningProgress progress = statisticsService.getKnowledgePointProgress(currentUserId, knowledgePointId);
        return Result.success(progress);
    }

    private Long getCurrentUserId(Long userId) {
        if (userId != null) {
            return userId;
        }
        log.warn("未获取到用户 ID，使用默认用户 1");
        return 1L;
    }
}