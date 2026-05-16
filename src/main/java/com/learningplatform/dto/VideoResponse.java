package com.learningplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoResponse {
    private Long id;
    private Long knowledgePointId;
    private String title;
    private String description;
    private String videoUrl;
    private Integer duration;
    private String thumbnailUrl;
    private Integer sortOrder;
    private LocalDateTime updateTime;
}
