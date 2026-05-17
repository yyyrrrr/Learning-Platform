package com.learningplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learningplatform.dto.StatisticsDTO.*;
import com.learningplatform.entity.Exercise;
import com.learningplatform.entity.KnowledgePoint;
import com.learningplatform.entity.LearningProgress;
import com.learningplatform.entity.LearningRecord;
import com.learningplatform.repository.ExerciseRepository;
import com.learningplatform.repository.KnowledgePointRepository;
import com.learningplatform.repository.LearningProgressRepository;
import com.learningplatform.repository.LearningRecordRepository;
import com.learningplatform.repository.UserExerciseRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(value = "transactionManager")
public class StatisticsService {

    @Autowired
    private LearningRecordRepository learningRecordRepository;

    @Autowired
    private LearningProgressRepository learningProgressRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    @Autowired
    private UserExerciseRecordRepository userExerciseRecordRepository;

    private static final List<String> MODULES = Arrays.asList(
            "集合", "线性表", "树", "图", "查找", "排序"
    );

    /**
     * 获取学习概况数据
     */
    public OverviewVO getOverview(Long userId) {
        OverviewVO overview = new OverviewVO();

        // 总学习时长
        Double totalHours = learningRecordRepository.getTotalHours(userId);
        overview.setTotalHours(totalHours != null ? totalHours : 0.0);

        // 已学知识点数
        Long learnedCount = learningProgressRepository.getLearnedKnowledgePoints(userId);
        overview.setLearnedCount(learnedCount != null ? learnedCount : 0L);

        // 总知识点数
        Long totalCount = knowledgePointRepository.selectCount(null);
        overview.setTotalCount(totalCount != null ? totalCount : 0L);

        // 做题统计
        Map<String, Object> exerciseStats = learningRecordRepository.getExerciseStats(userId);
        if (exerciseStats != null) {
            Object total = exerciseStats.get("total");
            Object correct = exerciseStats.get("correct");
            Long totalExercises = total != null ? ((Number) total).longValue() : 0L;
            Long correctExercises = correct != null ? ((Number) correct).longValue() : 0L;

            overview.setCompletedExercises(correctExercises);
            overview.setTotalExercises(totalExercises);

            // 计算正确率
            if (totalExercises > 0) {
                overview.setCorrectRate((correctExercises * 100.0) / totalExercises);
            } else {
                overview.setCorrectRate(0.0);
            }
        } else {
            overview.setCompletedExercises(0L);
            overview.setTotalExercises(0L);
            overview.setCorrectRate(0.0);
        }

        return overview;
    }

    /**
     * 获取学习进度分析数据
     */
    public List<ModuleProgressVO> getProgressAnalysis(Long userId) {
        List<ModuleProgressVO> result = new ArrayList<>();

        // 获取各模块进度
        List<Map<String, Object>> moduleProgress = learningProgressRepository.getModuleProgress(userId);

        // 获取各模块题目总数
        Map<String, Long> moduleExerciseTotal = getModuleExerciseTotal(userId);

        // 获取用户各模块题目完成情况
        Map<String, Long> moduleExerciseCompleted = getModuleExerciseCompleted(userId);

        // 构建结果
        for (String module : MODULES) {
            ModuleProgressVO vo = new ModuleProgressVO();
            vo.setModule(module);

            // 查找该模块的进度数据
            Map<String, Object> progressData = moduleProgress.stream()
                    .filter(m -> module.equals(m.get("module")))
                    .findFirst()
                    .orElse(null);

            if (progressData != null) {
                Object totalKp = progressData.get("total_kp");
                Object learnedKp = progressData.get("learned_kp");

                vo.setTotalKp(totalKp != null ? ((Number) totalKp).intValue() : 0);
                vo.setLearnedKp(learnedKp != null ? ((Number) learnedKp).intValue() : 0);

                // 计算知识点完成率
                if (vo.getTotalKp() > 0) {
                    vo.setKnowledgeRate((vo.getLearnedKp() * 100) / vo.getTotalKp());
                } else {
                    vo.setKnowledgeRate(0);
                }
            } else {
                vo.setTotalKp(0);
                vo.setLearnedKp(0);
                vo.setKnowledgeRate(0);
            }

            // 获取题目完成率
            Long exerciseTotal = moduleExerciseTotal.getOrDefault(module, 0L);
            Long exerciseCompleted = moduleExerciseCompleted.getOrDefault(module, 0L);
            vo.setExerciseRate(exerciseTotal > 0 ? (int) ((exerciseCompleted * 100) / exerciseTotal) : 0);

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取各模块题目总数
     */
    private Map<String, Long> getModuleExerciseTotal(Long userId) {
        Map<String, Long> result = new HashMap<>();
        
        // 查询每个模块的题目数
        for (String module : MODULES) {
            QueryWrapper<Exercise> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("knowledge_point_id", 
                knowledgePointRepository.selectList(new QueryWrapper<KnowledgePoint>().eq("module", module))
                    .stream().map(KnowledgePoint::getId).collect(Collectors.toList()));
            Long count = exerciseRepository.selectCount(queryWrapper);
            result.put(module, count);
        }
        
        return result;
    }

    /**
     * 获取用户各模块题目完成情况
     */
    private Map<String, Long> getModuleExerciseCompleted(Long userId) {
        Map<String, Long> result = new HashMap<>();
        
        // 查询用户各模块正确完成的题目数
        for (String module : MODULES) {
            List<KnowledgePoint> kps = knowledgePointRepository.selectList(
                new QueryWrapper<KnowledgePoint>().eq("module", module));
            
            if (!kps.isEmpty()) {
                List<Long> kpIds = kps.stream().map(KnowledgePoint::getId).collect(Collectors.toList());
                
                QueryWrapper<Exercise> exerciseQuery = new QueryWrapper<>();
                exerciseQuery.in("knowledge_point_id", kpIds);
                List<Long> exerciseIds = exerciseRepository.selectList(exerciseQuery)
                    .stream().map(Exercise::getId).collect(Collectors.toList());
                
                if (!exerciseIds.isEmpty()) {
                    QueryWrapper<UserExerciseRecord> recordQuery = new QueryWrapper<>();
                    recordQuery.eq("user_id", userId)
                               .eq("is_correct", true)
                               .in("exercise_id", exerciseIds);
                    Long count = userExerciseRecordRepository.selectCount(recordQuery);
                    result.put(module, count);
                } else {
                    result.put(module, 0L);
                }
            } else {
                result.put(module, 0L);
            }
        }
        
        return result;
    }

    /**
     * 获取各模块掌握度数据
     */
    public List<ModuleMasteryVO> getMasteryAnalysis(Long userId) {
        List<ModuleMasteryVO> result = new ArrayList<>();

        // 获取各模块掌握度
        List<Map<String, Object>> moduleMastery = learningProgressRepository.getModuleMastery(userId);

        // 构建结果
        for (String module : MODULES) {
            ModuleMasteryVO vo = new ModuleMasteryVO();
            vo.setModule(module);

            // 查找该模块的掌握度数据
            Map<String, Object> masteryData = moduleMastery.stream()
                    .filter(m -> module.equals(m.get("module")))
                    .findFirst()
                    .orElse(null);

            if (masteryData != null) {
                Object avgScore = masteryData.get("avg_score");
                vo.setScore(avgScore != null ? ((Number) avgScore).intValue() : 0);
            } else {
                vo.setScore(0);
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取学习趋势数据
     */
    public List<TrendDataVO> getTrendData(Long userId, int days) {
        List<TrendDataVO> result = new ArrayList<>();

        // 计算起始日期
        LocalDateTime startDate = LocalDate.now().minusDays(days).atStartOfDay();

        // 查询数据
        List<Map<String, Object>> trendData = learningRecordRepository.getTrendData(userId, startDate);

        // 构建结果
        for (Map<String, Object> data : trendData) {
            TrendDataVO vo = new TrendDataVO();
            Object dateObj = data.get("date");
            Object hoursObj = data.get("hours");

            if (dateObj != null) {
                vo.setDate(dateObj.toString());
            }
            vo.setHours(hoursObj != null ? ((Number) hoursObj).doubleValue() : 0.0);

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取薄弱知识点
     */
    public List<WeakPointVO> getWeakPoints(Long userId, int limit) {
        List<WeakPointVO> result = new ArrayList<>();

        // 查询薄弱知识点
        List<Map<String, Object>> weakPoints = learningProgressRepository.getWeakPoints(userId, limit);

        for (Map<String, Object> data : weakPoints) {
            WeakPointVO vo = new WeakPointVO();

            Object name = data.get("name");
            Object module = data.get("module");
            Object masteryScore = data.get("mastery_score");
            Object exerciseCount = data.get("exercise_count");
            Object correctCount = data.get("correct_count");

            vo.setName(name != null ? name.toString() : "");
            vo.setModule(module != null ? module.toString() : "");
            
            // 根据 mastery_score 推断掌握程度
            Integer score = masteryScore != null ? ((Number) masteryScore).intValue() : null;
            if (score == null) {
                vo.setMasteryLevel("未学习");
                vo.setMasteryScore(0);
            } else if (score >= 80) {
                vo.setMasteryLevel("掌握");
                vo.setMasteryScore(score);
            } else if (score >= 60) {
                vo.setMasteryLevel("基本掌握");
                vo.setMasteryScore(score);
            } else {
                vo.setMasteryLevel("未掌握");
                vo.setMasteryScore(score);
            }

            // 计算正确率
            int exCount = exerciseCount != null ? ((Number) exerciseCount).intValue() : 0;
            int cCount = correctCount != null ? ((Number) correctCount).intValue() : 0;
            vo.setCorrectRate(exCount > 0 ? (cCount * 100) / exCount : 0);

            // 生成建议
            if (vo.getMasteryScore() < 50) {
                vo.setSuggestion("建议复习");
            } else if (vo.getMasteryScore() < 70) {
                vo.setSuggestion("继续练习");
            } else {
                vo.setSuggestion("保持状态");
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取错题列表
     */
    public List<WrongQuestionVO> getWrongQuestions(Long userId, String module, int limit) {
        List<WrongQuestionVO> result = new ArrayList<>();

        // 获取错题 ID 列表
        List<Long> wrongExerciseIds = userExerciseRecordRepository.findWrongExerciseIds(userId);

        if (wrongExerciseIds.isEmpty()) {
            return result;
        }

        // 查询错题详情
        for (Long exerciseId : wrongExerciseIds) {
            Exercise exercise = exerciseRepository.selectById(exerciseId);
            if (exercise != null) {
                // 如果指定了模块，过滤
                if (module != null && !module.isEmpty()) {
                    KnowledgePoint kp = knowledgePointRepository.selectById(exercise.getKnowledgePointId());
                    if (kp == null || !module.equals(kp.getModule())) {
                        continue;
                    }
                }

                WrongQuestionVO vo = new WrongQuestionVO();
                vo.setExerciseId(exercise.getId());
                vo.setTitle(exercise.getTitle());
                vo.setType(exercise.getType());
                vo.setDifficulty(exercise.getDifficulty());

                // 获取知识点模块
                KnowledgePoint kp = knowledgePointRepository.selectById(exercise.getKnowledgePointId());
                if (kp != null) {
                    vo.setModule(kp.getModule());
                }

                // 统计提交次数
                QueryWrapper<UserExerciseRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId).eq("exercise_id", exerciseId);
                Long count = userExerciseRecordRepository.selectCount(queryWrapper);
                vo.setSubmitCount(count != null ? count.intValue() : 1);
                
                // 获取最后提交时间
                List<UserExerciseRecord> records = userExerciseRecordRepository.selectList(queryWrapper);
                if (!records.isEmpty()) {
                    LocalDateTime lastTime = records.get(records.size() - 1).getCreateTime();
                    vo.setLastSubmitTime(lastTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }

                result.add(vo);
            }
        }

        // 按限制返回
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 获取完整统计数据
     */
    public StatisticsVO getStatistics(Long userId) {
        StatisticsVO statistics = new StatisticsVO();

        statistics.setOverview(getOverview(userId));
        statistics.setProgress(getProgressAnalysis(userId));
        statistics.setMastery(getMasteryAnalysis(userId));
        statistics.setTrend(getTrendData(userId, 7)); // 默认 7 天
        statistics.setWeakPoints(getWeakPoints(userId, 10)); // 默认 10 个

        return statistics;
    }

    /**
     * 记录学习行为
     */
    public boolean recordLearningBehavior(LearningRecord record) {
        try {
            learningRecordRepository.insert(record);
            log.info("记录学习行为：userId={}, actionType={}, targetId={}", 
                record.getUserId(), record.getActionType(), record.getKnowledgePointId());
            return true;
        } catch (Exception e) {
            log.error("记录学习行为失败", e);
            return false;
        }
    }

    /**
     * 更新学习进度
     */
    public boolean updateLearningProgress(LearningProgress progress) {
        try {
            // 先查询是否已存在
            QueryWrapper<LearningProgress> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", progress.getUserId())
                       .eq("knowledge_point_id", progress.getKnowledgePointId());
            
            LearningProgress existing = learningProgressRepository.selectOne(queryWrapper);
            
            if (existing != null) {
                // 更新现有记录
                existing.setStatus(progress.getStatus());
                existing.setStudyDuration(progress.getStudyDuration());
                existing.setLastStudyTime(progress.getLastStudyTime());
                existing.setMasteryScore(progress.getMasteryScore());
                learningProgressRepository.updateById(existing);
            } else {
                // 插入新记录
                learningProgressRepository.insert(progress);
            }
            
            log.info("更新学习进度：userId={}, knowledgePointId={}, status={}", 
                progress.getUserId(), progress.getKnowledgePointId(), progress.getStatus());
            return true;
        } catch (Exception e) {
            log.error("更新学习进度失败", e);
            return false;
        }
    }

    /**
     * 获取用户的学习进度列表
     */
    public List<LearningProgress> getLearningProgress(Long userId) {
        QueryWrapper<LearningProgress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return learningProgressRepository.selectList(queryWrapper);
    }

    /**
     * 获取指定知识点的进度
     */
    public LearningProgress getKnowledgePointProgress(Long userId, Long knowledgePointId) {
        QueryWrapper<LearningProgress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("knowledge_point_id", knowledgePointId);
        return learningProgressRepository.selectOne(queryWrapper);
    }
}package com.learningplatform.service;

import com.learningplatform.dto.StatisticsDTO.*;
import com.learningplatform.entity.Exercise;
import com.learningplatform.entity.KnowledgePoint;
import com.learningplatform.entity.LearningProgress;
import com.learningplatform.entity.LearningRecord;
import com.learningplatform.repository.ExerciseRepository;
import com.learningplatform.repository.KnowledgePointRepository;
import com.learningplatform.repository.LearningProgressRepository;
import com.learningplatform.repository.LearningRecordRepository;
import com.learningplatform.repository.UserExerciseRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    private LearningRecordRepository learningRecordRepository;

    @Autowired
    private LearningProgressRepository learningProgressRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    @Autowired
    private UserExerciseRecordRepository userExerciseRecordRepository;

    private static final List<String> MODULES = Arrays.asList(
            "集合", "线性表", "树", "图", "查找", "排序"
    );

    /**
     * 获取学习概况数据
     */
    public OverviewVO getOverview(Long userId) {
        OverviewVO overview = new OverviewVO();

        // 总学习时长
        Double totalHours = learningRecordRepository.getTotalHours(userId);
        overview.setTotalHours(totalHours != null ? totalHours : 0.0);

        // 已学知识点数
        Long learnedCount = learningProgressRepository.getLearnedKnowledgePoints(userId);
        overview.setLearnedCount(learnedCount != null ? learnedCount : 0L);

        // 总知识点数
        Long totalCount = knowledgePointRepository.selectCount(null);
        overview.setTotalCount(totalCount != null ? totalCount : 0L);

        // 做题统计
        Map<String, Object> exerciseStats = learningRecordRepository.getExerciseStats(userId);
        if (exerciseStats != null) {
            Object total = exerciseStats.get("total");
            Object correct = exerciseStats.get("correct");
            Long totalExercises = total != null ? ((Number) total).longValue() : 0L;
            Long correctExercises = correct != null ? ((Number) correct).longValue() : 0L;

            overview.setCompletedExercises(correctExercises);
            overview.setTotalExercises(totalExercises);

            // 计算正确率
            if (totalExercises > 0) {
                overview.setCorrectRate((correctExercises * 100.0) / totalExercises);
            } else {
                overview.setCorrectRate(0.0);
            }
        } else {
            overview.setCompletedExercises(0L);
            overview.setTotalExercises(0L);
            overview.setCorrectRate(0.0);
        }

        return overview;
    }

    /**
     * 获取学习进度分析数据
     */
    public List<ModuleProgressVO> getProgressAnalysis(Long userId) {
        List<ModuleProgressVO> result = new ArrayList<>();

        // 获取各模块进度
        List<Map<String, Object>> moduleProgress = learningProgressRepository.getModuleProgress(userId);

        // 获取各模块题目总数
        Map<String, Long> moduleExerciseTotal = getModuleExerciseTotal();

        // 获取用户各模块题目完成情况
        Map<String, Long> moduleExerciseCompleted = getModuleExerciseCompleted(userId);

        // 构建结果
        for (String module : MODULES) {
            ModuleProgressVO vo = new ModuleProgressVO();
            vo.setModule(module);

            // 查找该模块的进度数据
            Map<String, Object> progressData = moduleProgress.stream()
                    .filter(m -> module.equals(m.get("module")))
                    .findFirst()
                    .orElse(null);

            if (progressData != null) {
                Object totalKp = progressData.get("total_kp");
                Object learnedKp = progressData.get("learned_kp");

                vo.setTotalKp(totalKp != null ? ((Number) totalKp).intValue() : 0);
                vo.setLearnedKp(learnedKp != null ? ((Number) learnedKp).intValue() : 0);

                // 计算知识点完成率
                if (vo.getTotalKp() > 0) {
                    vo.setKnowledgeRate((vo.getLearnedKp() * 100) / vo.getTotalKp());
                } else {
                    vo.setKnowledgeRate(0);
                }
            } else {
                vo.setTotalKp(0);
                vo.setLearnedKp(0);
                vo.setKnowledgeRate(0);
            }

            // 获取题目完成率
            Long exerciseTotal = moduleExerciseTotal.getOrDefault(module, 0L);
            Long exerciseCompleted = moduleExerciseCompleted.getOrDefault(module, 0L);
            vo.setExerciseRate(exerciseTotal > 0 ? (int) ((exerciseCompleted * 100) / exerciseTotal) : 0);

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取各模块题目总数
     */
    private Map<String, Long> getModuleExerciseTotal() {
        // 简单实现：统计每个模块的题目数
        Map<String, Long> result = new HashMap<>();
        for (String module : MODULES) {
            // 这里可以通过关联 knowledge_point 表统计
            // 简化处理，返回 0，实际应该查询数据库
            result.put(module, 0L);
        }
        return result;
    }

    /**
     * 获取用户各模块题目完成情况
     */
    private Map<String, Long> getModuleExerciseCompleted(Long userId) {
        Map<String, Long> result = new HashMap<>();
        for (String module : MODULES) {
            result.put(module, 0L);
        }
        return result;
    }

    /**
     * 获取各模块掌握度数据
     */
    public List<ModuleMasteryVO> getMasteryAnalysis(Long userId) {
        List<ModuleMasteryVO> result = new ArrayList<>();

        // 获取各模块掌握度
        List<Map<String, Object>> moduleMastery = learningProgressRepository.getModuleMastery(userId);

        // 构建结果
        for (String module : MODULES) {
            ModuleMasteryVO vo = new ModuleMasteryVO();
            vo.setModule(module);

            // 查找该模块的掌握度数据
            Map<String, Object> masteryData = moduleMastery.stream()
                    .filter(m -> module.equals(m.get("module")))
                    .findFirst()
                    .orElse(null);

            if (masteryData != null) {
                Object avgScore = masteryData.get("avg_score");
                vo.setScore(avgScore != null ? ((Number) avgScore).intValue() : 0);
            } else {
                vo.setScore(0);
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取学习趋势数据
     */
    public List<TrendDataVO> getTrendData(Long userId, int days) {
        List<TrendDataVO> result = new ArrayList<>();

        // 计算起始日期
        LocalDateTime startDate = LocalDate.now().minusDays(days).atStartOfDay();

        // 查询数据
        List<Map<String, Object>> trendData = learningRecordRepository.getTrendData(userId, startDate);

        // 构建结果
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Map<String, Object> data : trendData) {
            TrendDataVO vo = new TrendDataVO();
            Object dateObj = data.get("date");
            Object hoursObj = data.get("hours");

            if (dateObj != null) {
                vo.setDate(dateObj.toString());
            }
            vo.setHours(hoursObj != null ? ((Number) hoursObj).doubleValue() : 0.0);

            result.add(vo);
        }

        // 补充缺失的日期（没有学习记录的日期）
        // 简化处理，直接返回查询结果

        return result;
    }

    /**
     * 获取薄弱知识点
     */
    public List<WeakPointVO> getWeakPoints(Long userId, int limit) {
        List<WeakPointVO> result = new ArrayList<>();

        // 查询薄弱知识点
        List<Map<String, Object>> weakPoints = learningProgressRepository.getWeakPoints(userId, limit);

        for (Map<String, Object> data : weakPoints) {
            WeakPointVO vo = new WeakPointVO();

            Object name = data.get("name");
            Object module = data.get("module");
            Object masteryLevel = data.get("mastery_level");
            Object masteryScore = data.get("mastery_score");
            Object exerciseCount = data.get("exercise_count");
            Object correctCount = data.get("correct_count");

            vo.setName(name != null ? name.toString() : "");
            vo.setModule(module != null ? module.toString() : "");
            vo.setMasteryLevel(masteryLevel != null ? masteryLevel.toString() : "未知");
            vo.setMasteryScore(masteryScore != null ? ((Number) masteryScore).intValue() : 0);

            // 计算正确率
            int exCount = exerciseCount != null ? ((Number) exerciseCount).intValue() : 0;
            int cCount = correctCount != null ? ((Number) correctCount).intValue() : 0;
            vo.setCorrectRate(exCount > 0 ? (cCount * 100) / exCount : 0);

            // 生成建议
            if (vo.getMasteryScore() < 50) {
                vo.setSuggestion("建议复习");
            } else if (vo.getMasteryScore() < 70) {
                vo.setSuggestion("继续练习");
            } else {
                vo.setSuggestion("保持状态");
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取错题列表
     */
    public List<WrongQuestionVO> getWrongQuestions(Long userId, String module, int limit) {
        List<WrongQuestionVO> result = new ArrayList<>();

        // 获取错题 ID 列表
        List<Long> wrongExerciseIds = userExerciseRecordRepository.findWrongExerciseIds(userId);

        if (wrongExerciseIds.isEmpty()) {
            return result;
        }

        // 查询错题详情
        for (Long exerciseId : wrongExerciseIds) {
            Exercise exercise = exerciseRepository.selectById(exerciseId);
            if (exercise != null) {
                // 如果指定了模块，过滤
                if (module != null && !module.isEmpty()) {
                    KnowledgePoint kp = knowledgePointRepository.selectById(exercise.getKnowledgePointId());
                    if (kp == null || !module.equals(kp.getModule())) {
                        continue;
                    }
                }

                WrongQuestionVO vo = new WrongQuestionVO();
                vo.setExerciseId(exercise.getId());
                vo.setTitle(exercise.getTitle());
                vo.setType(exercise.getType());
                vo.setDifficulty(exercise.getDifficulty());

                // 获取知识点模块
                KnowledgePoint kp = knowledgePointRepository.selectById(exercise.getKnowledgePointId());
                if (kp != null) {
                    vo.setModule(kp.getModule());
                }

                // 统计提交次数
                // 简化处理，实际应该查询数据库
                vo.setSubmitCount(1);
                vo.setLastSubmitTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                result.add(vo);
            }
        }

        // 按限制返回
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 获取完整统计数据
     */
    public StatisticsVO getStatistics(Long userId) {
        StatisticsVO statistics = new StatisticsVO();

        statistics.setOverview(getOverview(userId));
        statistics.setProgress(getProgressAnalysis(userId));
        statistics.setMastery(getMasteryAnalysis(userId));
        statistics.setTrend(getTrendData(userId, 7)); // 默认 7 天
        statistics.setWeakPoints(getWeakPoints(userId, 10)); // 默认 10 个

        return statistics;
    }
}