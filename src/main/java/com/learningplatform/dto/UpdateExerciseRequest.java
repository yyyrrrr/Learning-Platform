package com.learningplatform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateExerciseRequest {
    @NotNull(message = "习题ID不能为空")
    private Long id;

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
    private Integer sortOrder;
}
