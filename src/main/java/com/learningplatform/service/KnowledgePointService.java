package com.learningplatform.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learningplatform.dto.KnowledgePointTreeNode;
import com.learningplatform.entity.KnowledgePoint;
import com.learningplatform.repository.KnowledgePointRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KnowledgePointService extends ServiceImpl<KnowledgePointRepository, KnowledgePoint> {

    public List<KnowledgePointTreeNode> getTree() {
        List<KnowledgePoint> all = list();
        if (all.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<KnowledgePoint>> childrenMap = all.stream()
                .filter(kp -> kp.getParentId() != null)
                .collect(Collectors.groupingBy(KnowledgePoint::getParentId));

        return all.stream()
                .filter(kp -> kp.getParentId() == null)
                .map(kp -> buildTreeNode(kp, childrenMap))
                .collect(Collectors.toList());
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
