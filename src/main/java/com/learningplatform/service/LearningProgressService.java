package com.learningplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learningplatform.entity.LearningProgress;
import com.learningplatform.repository.LearningProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LearningProgressService {

    @Autowired
    private LearningProgressRepository learningProgressRepository;

    private static final Map<String, String> STATUS_MAP = Map.of(
            "NOT_STARTED", "not_started",
            "LEARNING", "in_progress",
            "COMPLETED", "completed",
            "MASTERED", "completed"
    );

    public Map<String, Object> getProgressMap(Long userId) {
        QueryWrapper<LearningProgress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<LearningProgress> list = learningProgressRepository.selectList(wrapper);

        Map<String, String> progressMap = new HashMap<>();
        for (LearningProgress lp : list) {
            String frontendStatus = STATUS_MAP.getOrDefault(lp.getStatus(), "not_started");
            progressMap.put(String.valueOf(lp.getKnowledgePointId()), frontendStatus);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("progressMap", progressMap);
        return result;
    }
}
