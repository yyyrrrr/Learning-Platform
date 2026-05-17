package com.learningplatform.controller;

import com.learningplatform.common.Result;
import com.learningplatform.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/learning", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
public class LearningController {

    @Autowired
    private LearningProgressService learningProgressService;

    @GetMapping("/progress")
    public Result<Map<String, Object>> getProgress(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long currentUserId = userId != null ? userId : 1L;
        Map<String, Object> progressMap = learningProgressService.getProgressMap(currentUserId);
        return Result.success(progressMap);
    }
}
