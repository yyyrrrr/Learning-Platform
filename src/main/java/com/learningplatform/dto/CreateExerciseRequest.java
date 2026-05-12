package com.learningplatform.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateExerciseRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotNull(message = "知识点ID不能为空")
    private Long knowledgePointId;

    private String description;

    @NotBlank(message = "题型不能为空")
    private String type;

    @NotBlank(message = "难度不能为空")
    private String difficulty;

    private String category = "BASIC";

    private String options;

    private String answer;

    private String analysis;

    private String initialCode;

    private String testCases;

    @Min(value = 1, message = "时间限制至少1秒")
    private Integer timeLimit = 60;

    private Integer sortOrder = 0;
}
