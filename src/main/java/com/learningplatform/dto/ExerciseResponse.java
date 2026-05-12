package com.learningplatform.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExerciseResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String difficulty;
    private String category;
    private Double passRate;
    private Integer timeLimit;
    private LocalDateTime updateTime;
    private String knowledgeName;
    private String userStatus;  // unattempted, attempted, passed
}

