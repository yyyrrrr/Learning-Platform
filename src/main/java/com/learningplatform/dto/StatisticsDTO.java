package com.learningplatform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 学习统计相关 DTO
 */
public class StatisticsDTO {

    /**
     * 学习概况数据
     */
    @Data
    public static class OverviewVO {
        private Double totalHours;           // 总学习时长（小时）
        private Long learnedCount;           // 已学知识点数
        private Long totalCount;             // 总知识点数
        private Long completedExercises;     // 已完成题目数
        private Long totalExercises;         // 总题目数
        private Double correctRate;          // 平均正确率（0-100）
    }

    /**
     * 模块进度数据
     */
    @Data
    public static class ModuleProgressVO {
        private String module;               // 模块名称
        private Integer totalKp;             // 该模块总知识点数
        private Integer learnedKp;           // 已学知识点数
        private Integer knowledgeRate;       // 知识点完成率（0-100）
        private Integer exerciseRate;        // 题目完成率（0-100）
    }

    /**
     * 模块掌握度数据
     */
    @Data
    public static class ModuleMasteryVO {
        private String module;               // 模块名称
        private Integer score;               // 掌握度评分（0-100）
    }

    /**
     * 学习趋势数据
     */
    @Data
    public static class TrendDataVO {
        private String date;                 // 日期
        private Double hours;                // 学习时长（小时）
    }

    /**
     * 薄弱知识点数据
     */
    @Data
    public static class WeakPointVO {
        private String name;                 // 知识点名称
        private String module;               // 所属模块
        private String masteryLevel;         // 掌握程度
        private Integer masteryScore;        // 掌握度分数
        private Integer correctRate;         // 正确率（0-100）
        private String suggestion;           // 学习建议
    }

    /**
     * 错题数据
     */
    @Data
    public static class WrongQuestionVO {
        private Long exerciseId;             // 题目 ID
        private String title;                // 题目标题
        private String module;               // 所属模块
        private String type;                 // 题型
        private String difficulty;           // 难度
        private Integer submitCount;         // 提交次数
        private String lastSubmitTime;       // 最后提交时间
    }

    /**
     * 完整统计数据
     */
    @Data
    public static class StatisticsVO {
        private OverviewVO overview;                    // 学习概况
        private List<ModuleProgressVO> progress;        // 学习进度
        private List<ModuleMasteryVO> mastery;          // 掌握度
        private List<TrendDataVO> trend;                // 学习趋势
        private List<WeakPointVO> weakPoints;           // 薄弱知识点
    }
}