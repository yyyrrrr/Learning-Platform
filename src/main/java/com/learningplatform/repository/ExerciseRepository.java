package com.learningplatform.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learningplatform.entity.Exercise;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExerciseRepository extends BaseMapper<Exercise> {

    @Select("SELECT e.* FROM exercise e " +
            "INNER JOIN user_exercise_record r ON e.id = r.exercise_id AND r.user_id = #{userId} " +
            "WHERE e.is_deleted = 0 AND r.is_correct = 0")
    List<Exercise> findWrongExercisesByUserId(@Param("userId") Long userId);

}