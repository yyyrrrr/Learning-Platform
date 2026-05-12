package com.learningplatform.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//import javax.validation.constraints.*;

@Data
public class ExerciseQueryRequest {
    private Long knowledgeId;
    private String difficulty;
    private String type;
    private String keyword;
    private Integer page = 1;
    private Integer pageSize = 20;
}

