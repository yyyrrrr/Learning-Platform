package com.learningplatform.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learningplatform.dto.KnowledgePointTreeNode;
import com.learningplatform.entity.KnowledgePoint;
import com.learningplatform.repository.KnowledgePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KnowledgePointService extends ServiceImpl<KnowledgePointRepository, KnowledgePoint> {

    @Autowired
    private GraphService graphService;

    public Map<String, Object> getPrerequisites(Long knowledgePointId) {
        KnowledgePoint kp = getById(knowledgePointId);
        if (kp == null || kp.getNeo4jId() == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("prerequisites", Collections.emptyList());
            return result;
        }

        List<Map<String, Object>> relatedNodes = graphService.getRelatedNodes(kp.getNeo4jId());

        List<Map<String, Object>> prerequisites = new ArrayList<>();
        for (Map<String, Object> node : relatedNodes) {
            String neo4jId = (String) node.get("neo4jId");
            KnowledgePoint prereqKp = lambdaQuery()
                    .eq(KnowledgePoint::getNeo4jId, neo4jId)
                    .one();

            Map<String, Object> prereq = new HashMap<>();
            prereq.put("neo4jId", neo4jId);
            prereq.put("name", node.get("name"));
            prereq.put("id", prereqKp != null ? prereqKp.getId() : null);
            prerequisites.add(prereq);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("prerequisites", prerequisites);
        return result;
    }

    public List<KnowledgePointTreeNode> getTree() {
        List<KnowledgePoint> all = list();
        if (all.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<KnowledgePoint>> childrenMap = all.stream()
                .filter(kp -> kp.getParentId() != null)
                .collect(Collectors.groupingBy(KnowledgePoint::getParentId));

        List<KnowledgePointTreeNode> tree = all.stream()
                .filter(kp -> kp.getParentId() == null)
                .map(kp -> buildTreeNode(kp, childrenMap))
                .collect(Collectors.toList());

        List<String> neo4jIds = all.stream()
                .map(KnowledgePoint::getNeo4jId)
                .filter(id -> id != null && !id.isEmpty())
                .collect(Collectors.toList());

        if (!neo4jIds.isEmpty()) {
            Map<String, List<String>> prereqMap = graphService.batchGetRelatedNodeIds(neo4jIds);
            populatePrerequisites(tree, prereqMap);
        }

        return tree;
    }

    private void populatePrerequisites(List<KnowledgePointTreeNode> nodes, Map<String, List<String>> prereqMap) {
        for (KnowledgePointTreeNode node : nodes) {
            if (node.getNeo4jId() != null) {
                List<String> prereqs = prereqMap.getOrDefault(node.getNeo4jId(), Collections.emptyList());
                node.setPrerequisites(prereqs);
            }
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                populatePrerequisites(node.getChildren(), prereqMap);
            }
        }
    }

    private KnowledgePointTreeNode buildTreeNode(KnowledgePoint kp, Map<Long, List<KnowledgePoint>> childrenMap) {
        KnowledgePointTreeNode node = new KnowledgePointTreeNode();
        node.setId(kp.getId());
        node.setNeo4jId(kp.getNeo4jId());
        node.setName(kp.getName());
        node.setCode(kp.getCode());
        node.setModule(kp.getModule());
        node.setLevel(kp.getLevel());
        node.setDifficulty(kp.getDifficulty());

        List<KnowledgePoint> children = childrenMap.getOrDefault(kp.getId(), Collections.emptyList());
        List<KnowledgePointTreeNode> childNodes = children.stream()
                .map(child -> buildTreeNode(child, childrenMap))
                .collect(Collectors.toList());
        node.setChildren(childNodes);

        return node;
    }
}
