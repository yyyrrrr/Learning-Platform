package com.learningplatform.controller;

import com.learningplatform.common.Result;
import com.learningplatform.service.LearningProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/learning", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Slf4j
public class LearningController {

    @Autowired
    private LearningProgressService learningProgressService;

    @GetMapping("/progress")
    public Result<Map<String, Object>> getProgress(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = getCurrentUserId(userId);
        Map<String, Object> progressMap = learningProgressService.getProgressMap(currentUserId);
        return Result.success(progressMap);
    }

    private Long getCurrentUserId(Long userId) {
        if (userId != null) {
            return userId;
        }
        log.warn("未获取到用户 ID，使用默认用户 1");
        return 1L;
    }
}
