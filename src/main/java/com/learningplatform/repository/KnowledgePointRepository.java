package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KnowledgePointRepository extends BaseMapper<KnowledgePoint> {
}
