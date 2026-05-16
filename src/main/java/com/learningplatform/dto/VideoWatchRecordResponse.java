package com.learningplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoWatchRecordResponse {
    private Long id;
    private Long userId;
    private Long videoId;
    private Integer watchProgress;
    private Integer watchDuration;
    private LocalDateTime lastWatchTime;
    private Integer isCompleted;
}
