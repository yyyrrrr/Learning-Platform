package com.learningplatform.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learningplatform.entity.KnowledgePoint;
import com.learningplatform.repository.KnowledgePointRepository;
import org.springframework.stereotype.Service;

@Service
public class KnowledgePointService extends ServiceImpl<KnowledgePointRepository, KnowledgePoint> {
}
