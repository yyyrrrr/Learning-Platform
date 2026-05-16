package com.learningplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VideoWatchRecordRequest {

    @NotNull(message = "videoId cannot be null")
    private Long videoId;

    @NotNull(message = "watchProgress cannot be null")
    @Min(value = 0, message = "watchProgress cannot be less than 0")
    @Max(value = 100, message = "watchProgress cannot be greater than 100")
    private Integer watchProgress;

    @Min(value = 0, message = "watchDuration cannot be less than 0")
    private Integer watchDuration = 0;
}
