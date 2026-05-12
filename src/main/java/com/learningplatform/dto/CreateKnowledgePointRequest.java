package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateKnowledgePointRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String module;

    @NotNull
    private Integer level;

    @NotBlank
    private String difficulty;

    private Long parentId;

    private String description;

    private String corePoints;
}
