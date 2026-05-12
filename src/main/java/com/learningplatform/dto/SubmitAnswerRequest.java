package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitAnswerRequest {
    @NotNull(message = "习题ID不能为空")
    private Long exerciseId;

    @NotBlank(message = "答案不能为空")
    private String userAnswer;

    private String code;  // 编程题代码
}
