package com.learningplatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseListResponse {
    private List<ExerciseResponse> records;
    private Long total;
    private Integer page;
    private Integer pageSize;
}
