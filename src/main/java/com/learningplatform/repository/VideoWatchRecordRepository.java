package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.VideoWatchRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoWatchRecordRepository extends BaseMapper<VideoWatchRecord> {
}
