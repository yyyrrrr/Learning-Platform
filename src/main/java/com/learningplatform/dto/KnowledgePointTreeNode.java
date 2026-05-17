package com.learningplatform.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KnowledgePointTreeNode {
    private Long id;
    private String neo4jId;
    private String name;
    private String code;
    private String module;
    private Integer level;
    private String difficulty;
    private List<String> prerequisites = new ArrayList<>();
    private List<KnowledgePointTreeNode> children = new ArrayList<>();
}
