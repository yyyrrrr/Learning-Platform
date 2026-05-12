package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.UserExerciseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserExerciseRecordRepository extends BaseMapper<UserExerciseRecord> {

    @Select("SELECT DISTINCT exercise_id FROM user_exercise_record " +
            "WHERE user_id = #{userId} AND is_correct = 0")
    List<Long> findWrongExerciseIds(@Param("userId") Long userId);
}