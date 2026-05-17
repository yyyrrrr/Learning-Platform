package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.LearningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface LearningRecordRepository extends BaseMapper<LearningRecord> {

    /**
     * 统计用户总学习时长
     */
    @Select("SELECT COALESCE(SUM(duration), 0) / 3600.0 as hours " +
            "FROM learning_record " +
            "WHERE user_id = #{userId}")
    Double getTotalHours(@Param("userId") Long userId);

    /**
     * 获取用户学习趋势（按日期统计）
     */
    @Select("SELECT DATE(create_time) as date, " +
            "SUM(duration) / 3600.0 as hours " +
            "FROM learning_record " +
            "WHERE user_id = #{userId} " +
            "AND create_time >= #{startDate} " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date")
    List<Map<String, Object>> getTrendData(@Param("userId") Long userId, 
                                           @Param("startDate") LocalDateTime startDate);

    /**
     * 统计用户已学知识点数
     */
    @Select("SELECT COUNT(DISTINCT knowledge_point_id) " +
            "FROM learning_progress " +
            "WHERE user_id = #{userId} AND status != 'NOT_STARTED'")
    Long getLearnedKnowledgePoints(@Param("userId") Long userId);

    /**
     * 统计用户做题情况
     */
    @Select("SELECT COUNT(*) as total, " +
            "SUM(CASE WHEN is_correct = 1 THEN 1 ELSE 0 END) as correct " +
            "FROM user_exercise_record " +
            "WHERE user_id = #{userId}")
    Map<String, Object> getExerciseStats(@Param("userId") Long userId);
}