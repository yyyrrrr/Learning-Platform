package com.learningplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_point")
public class KnowledgePoint extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String neo4jId;

    private String name;

    private String code;

    private Long parentId;

    private String module;

    private Integer level;

    private String difficulty;

    private String description;

    private String corePoints;
}
