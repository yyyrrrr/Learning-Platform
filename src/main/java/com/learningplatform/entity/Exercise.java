package com.learningplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercise")
public class Exercise extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long knowledgePointId;

    private String title;

    private String type;

    private String difficulty;

    private String category;

    private String options;

    private String answer;

    private String analysis;

    private Integer timeLimit;

    private Integer sortOrder;
}
