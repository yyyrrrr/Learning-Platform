package com.learningplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("learning_progress")
public class LearningProgress extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long knowledgePointId;

    private String status;

    private Integer studyDuration;

    private LocalDateTime lastStudyTime;

    private Integer masteryScore;
}
