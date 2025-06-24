package com.sirius.mapper;

import com.sirius.entity.QuestionEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    // 删除题目
    @Delete("DELETE FROM question_bank WHERE uid = #{uid}")
    void deleteQuestionsByUid(Integer uid);

    // 新增题目
    @Insert("INSERT INTO question_bank (type, description, options, answer, explanation, difficulty, tags, created_at, updated_at) " +
            "VALUES (#{type}, #{description}, #{options}, #{answer}, #{explanation}, #{difficulty}, #{tags}, NOW(), NOW())")
    void insertQuestion(QuestionEntity questionEntity);

    // 更改题目信息
    @Update("UPDATE question_bank SET " +
            "type = #{type}, description = #{description}, options = #{options}, answer = #{answer}, " +
            "explanation = #{explanation}, difficulty = #{difficulty}, tags = #{tags}, updated_at = NOW() " +
            "WHERE uid = #{uid}")
    void updateQuestionByUid(QuestionEntity questionEntity);

    // 1-获取题目数量
    @Select("SELECT COUNT(*) FROM question_bank")
    Integer getAllQuestionsCount();
    // 1-分页查询题目
    @Select("SELECT * FROM question_bank LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByPage(Integer startIndex, Integer pageSize);

    // 2- 按照题目信息获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%')")
    Integer getQuestionsCountByInformation(String information);
    // 2-按题目信息查询题目
    @Select("SELECT * FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByInformation(Integer startIndex, Integer pageSize, String information);

    // 3-按照题目类型获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE type = #{type}")
    Integer getQuestionsCountByType(String type);
    // 3-按照题目类型查询题目
    @Select("SELECT * FROM question_bank WHERE type = #{type} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByType(Integer startIndex, Integer pageSize, String type);

    // 4-按照题目难度获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE difficulty = #{difficulty}")
    Integer getQuestionsCountByDifficulty(Integer difficulty);
    // 4-按照题目难度查询题目
    @Select("SELECT * FROM question_bank WHERE difficulty = #{difficulty} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByDifficulty(Integer startIndex, Integer pageSize, Integer difficulty);

    // 5-按照题目信息和题目类型获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND type = #{type}")
    Integer getQuestionsCountByInformationAndType(String information, String type);
    // 5-按照题目信息和题目类型查询题目
    @Select("SELECT * FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND type = #{type} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByInformationAndType(Integer startIndex, Integer pageSize, String information, String type);

    // 6-按照题目信息和题目难度获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND difficulty = #{difficulty}")
    Integer getQuestionsCountByInformationAndDifficulty(String information, Integer difficulty);
    // 6-按照题目信息和题目难度查询题目
    @Select("SELECT * FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND difficulty = #{difficulty} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByInformationAndDifficulty(Integer startIndex, Integer pageSize, String information, Integer difficulty);

    // 7-按照题目类型和题目难度获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE type = #{type} AND difficulty = #{difficulty}")
    Integer getQuestionsCountByTypeAndDifficulty(String type, Integer difficulty);
    // 7-按照题目类型和题目难度查询题目
    @Select("SELECT * FROM question_bank WHERE type = #{type} AND difficulty = #{difficulty} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByTypeAndDifficulty(Integer startIndex, Integer pageSize, String type, Integer difficulty);

    // 8-按照题目信息、题目类型和题目难度获取数量
    @Select("SELECT COUNT(*) FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND type = #{type} AND difficulty = #{difficulty}")
    Integer getQuestionsCountByInformationAndTypeAndDifficulty(String information, String type, Integer difficulty);
    // 8-按照题目信息、题目类型和题目难度查询题目
    @Select("SELECT * FROM question_bank WHERE description LIKE CONCAT('%', #{information}, '%') AND type = #{type} AND difficulty = #{difficulty} LIMIT #{startIndex}, #{pageSize}")
    List<QuestionEntity> getQuestionsByInformationAndTypeAndDifficulty(Integer startIndex, Integer pageSize, String information, String type, Integer difficulty);

}
