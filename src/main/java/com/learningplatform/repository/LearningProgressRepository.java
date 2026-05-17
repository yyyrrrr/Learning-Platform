package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.LearningProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LearningProgressRepository extends BaseMapper<LearningProgress> {

    /**
     * 统计用户各模块的学习进度
     */
    @Select("SELECT kp.module, " +
            "COUNT(DISTINCT kp.id) as total_kp, " +
            "COUNT(DISTINCT lp.knowledge_point_id) as learned_kp " +
            "FROM knowledge_point kp " +
            "LEFT JOIN learning_progress lp ON kp.id = lp.knowledge_point_id AND lp.user_id = #{userId} " +
            "WHERE kp.is_deleted = 0 " +
            "GROUP BY kp.module")
    List<Map<String, Object>> getModuleProgress(@Param("userId") Long userId);

    /**
     * 获取用户各模块的掌握度（平均分）
     */
    @Select("SELECT kp.module, AVG(lp.mastery_score) as avg_score " +
            "FROM knowledge_point kp " +
            "LEFT JOIN learning_progress lp ON kp.id = lp.knowledge_point_id AND lp.user_id = #{userId} " +
            "WHERE kp.is_deleted = 0 AND lp.mastery_score IS NOT NULL " +
            "GROUP BY kp.module")
    List<Map<String, Object>> getModuleMastery(@Param("userId") Long userId);

    /**
     * 获取用户的薄弱知识点（掌握度分数<60 或没有记录）
     */
    @Select("SELECT kp.name, kp.module, " +
            "lp.mastery_score, " +
            "(SELECT COUNT(*) FROM user_exercise_record uer " +
            " JOIN exercise e ON uer.exercise_id = e.id " +
            " WHERE e.knowledge_point_id = kp.id AND uer.user_id = #{userId}) as exercise_count, " +
            "(SELECT COUNT(*) FROM user_exercise_record uer " +
            " JOIN exercise e ON uer.exercise_id = e.id " +
            " WHERE e.knowledge_point_id = kp.id AND uer.user_id = #{userId} AND uer.is_correct = 1) as correct_count " +
            "FROM learning_progress lp " +
            "JOIN knowledge_point kp ON lp.knowledge_point_id = kp.id " +
            "WHERE lp.user_id = #{userId} AND (lp.mastery_score < 60 OR lp.mastery_score IS NULL) " +
            "ORDER BY lp.mastery_score ASC NULLS FIRST " +
            "LIMIT #{limit}")
    List<Map<String, Object>> getWeakPoints(@Param("userId") Long userId, @Param("limit") int limit);
}