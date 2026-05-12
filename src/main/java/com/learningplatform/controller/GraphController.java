package com.learningplatform.controller;

import com.learningplatform.common.Result;
import com.learningplatform.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @GetMapping("/data")
    public Result<Map<String, Object>> getGraphData() {
        Map<String, Object> data = graphService.getGraphData();
        return Result.success(data);
    }

    @GetMapping("/node/{id}")
    public Result<Map<String, Object>> getNodeDetail(@PathVariable String id) {
        Map<String, Object> node = graphService.getNodeDetail(id);
        if (node == null) {
            return Result.error("知识点不存在");
        }
        return Result.success(node);
    }

    @GetMapping("/search")
    public Result<List<Map<String, Object>>> searchNodes(@RequestParam String keyword) {
        List<Map<String, Object>> nodes = graphService.searchNodes(keyword);
        return Result.success(nodes);
    }
}
