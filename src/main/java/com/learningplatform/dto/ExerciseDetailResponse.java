package com.learningplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseDetailResponse {
    private Long id;
    private Long knowledgePointId;
    private String title;
    private String description;
    private String type;
    private String difficulty;
    private String category;
    private String options;
    private String answer;
    private String analysis;
    private String initialCode;
    private String testCases;
    private Integer timeLimit;
    private Double passRate;
    private Integer totalAttempts;
    private Integer correctAttempts;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String knowledgeName;
    private String userAnswer;  // 用户之前的答案
    private String userStatus;  // 用户完成状态
}
