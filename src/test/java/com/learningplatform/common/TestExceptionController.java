package com.learningplatform.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestExceptionController {

    @GetMapping("/test/business")
    public String business() {
        throw new BusinessException("业务错误");
    }

    @GetMapping("/test/system")
    public String system() {
        throw new RuntimeException("系统错误");
    }
}
