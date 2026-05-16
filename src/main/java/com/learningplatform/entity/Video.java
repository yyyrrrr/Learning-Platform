package com.learningplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("video")
public class Video extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long knowledgePointId;

    private String title;

    private String description;

    private String videoUrl;

    private Integer duration;

    private String thumbnailUrl;

    private Integer sortOrder;
}
