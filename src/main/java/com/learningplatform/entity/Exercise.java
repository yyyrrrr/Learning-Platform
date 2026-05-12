package com.learningplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
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

    private String description;

    private String type;  // SINGLE_CHOICE, MULTIPLE_CHOICE, PROGRAMMING, FILL_BLANK

    private String difficulty;  // EASY, MEDIUM, HARD

    private String category;  // BASIC, INTERVIEW, EXAM

    @TableField("`options`")
    private String options;  // JSON格式存储选项，如 ["A. 选项1", "B. 选项2"]

    private String answer;  // 正确答案

    private String analysis;  // 题目解析

    private String initialCode;  // 编程题初始代码

    private String testCases;  // JSON格式存储测试用例

    private Integer timeLimit;  // 时间限制(秒)

    private Integer sortOrder;  // 排序字段

    private Double passRate;  // 通过率

    private Integer totalAttempts;  // 总尝试次数

    private Integer correctAttempts;  // 正确次数
}