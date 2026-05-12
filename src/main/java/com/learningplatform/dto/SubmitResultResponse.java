package com.learningplatform.dto;

import lombok.Data;

@Data
public class SubmitResultResponse {
    private boolean correct;
    private String correctAnswer;
    private String analysis;
    private Double passRate;
    private Long exerciseId;
}