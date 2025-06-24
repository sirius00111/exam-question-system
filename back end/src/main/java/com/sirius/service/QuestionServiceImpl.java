package com.sirius.service;

import com.sirius.entity.QuestionEntity;
import com.sirius.entity.QuestionStatsEntity;
import com.sirius.mapper.QuestionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    QuestionMapper questionMapper;

    @Override
    public QuestionStatsEntity getQuestionStats() {
        Integer easyNumber = questionMapper.getQuestionsCountByDifficulty(1);
        Integer mediumNumber = questionMapper.getQuestionsCountByDifficulty(2);
        Integer hardNumber = questionMapper.getQuestionsCountByDifficulty(3);
        Integer singleChoiceNumber = questionMapper.getQuestionsCountByType("single_choice");
        Integer multipleChoiceNumber = questionMapper.getQuestionsCountByType("multiple_choice");
        Integer trueFalseNumber = questionMapper.getQuestionsCountByType("true_false");
        Integer shortAnswerNumber = questionMapper.getQuestionsCountByType("short_answer");
        return new QuestionStatsEntity(easyNumber, mediumNumber, hardNumber, singleChoiceNumber, multipleChoiceNumber, trueFalseNumber, shortAnswerNumber);
    }

    @Override
    public List<QuestionEntity> getQuestions(Integer currentPageNumber, Integer pageSize, String information, String type, Integer difficulty) {
        // 处理分页参数-计算开始索引
        Integer startIndex = (currentPageNumber - 1) * pageSize;
        // 调用不同的查询方法
        if (Objects.equals(information, "")) {
            if (Objects.equals(type, "")) {
                if (difficulty == 0) {
                    // 0-0-0 分页查询题目
                    return questionMapper.getQuestionsByPage(startIndex, pageSize);
                } else {
                    // 0-0-1 按照题目难度查询题目
                    return questionMapper.getQuestionsByDifficulty(startIndex, pageSize, difficulty);
                }
            } else {
                if (difficulty == 0) {
                    // 0-1-0 按照题目类型查询题目
                    return questionMapper.getQuestionsByType(startIndex, pageSize, type);
                } else {
                    // 0-1-1 按照题目类型和题目难度查询题目
                    return questionMapper.getQuestionsByTypeAndDifficulty(startIndex, pageSize, type, difficulty);
                }
            }
        } else {
            if (Objects.equals(type, "")) {
                if (difficulty == 0) {
                    // 1-0-0 按照题目信息查询题目
                    return questionMapper.getQuestionsByInformation(startIndex, pageSize, information);
                } else {
                    // 1-0-1 按照题目信息和题目难度查询题目
                    return questionMapper.getQuestionsByInformationAndDifficulty(startIndex, pageSize, information, difficulty);
                }
            } else {
                if (difficulty == 0) {
                    // 1-1-0 按照题目信息和题目类型查询题目
                    return questionMapper.getQuestionsByInformationAndType(startIndex, pageSize, information, type);
                } else {
                    // 1-1-1 按照题目信息、题目类型和题目难度查询题目
                    return questionMapper.getQuestionsByInformationAndTypeAndDifficulty(startIndex, pageSize, information, type, difficulty);
                }
            }
        }
    }

    @Override
    public Integer getQuestionsCount(String information, String type, Integer difficulty) {
        // 调用不同的查询数量方法
        if (Objects.equals(information, "")) {
            if (Objects.equals(type, "")) {
                if (difficulty == 0) {
                    // 0-0-0 分页查询题目数量
                    return questionMapper.getAllQuestionsCount();
                } else {
                    // 0-0-1 按照题目难度查询题目数量
                    return questionMapper.getQuestionsCountByDifficulty(difficulty);
                }
            } else {
                if (difficulty == 0) {
                    // 0-1-0 按照题目类型查询题目数量
                    return questionMapper.getQuestionsCountByType(type);
                } else {
                    // 0-1-1 按照题目类型和题目难度查询题目数量
                    return questionMapper.getQuestionsCountByTypeAndDifficulty(type, difficulty);
                }
            }
        } else {
            if (Objects.equals(type, "")) {
                if (difficulty == 0) {
                    // 1-0-0 按照题目信息查询题目数量
                    return questionMapper.getQuestionsCountByInformation(information);
                } else {
                    // 1-0-1 按照题目信息和题目难度查询题目数量
                    return questionMapper.getQuestionsCountByInformationAndDifficulty(information, difficulty);
                }
            } else {
                if (difficulty == 0) {
                    // 1-1-0 按照题目信息和题目类型查询题目数量
                    return questionMapper.getQuestionsCountByInformationAndType(information, type);
                } else {
                    // 1-1-1 按照题目信息、题目类型和题目难度查询题目数量
                    return questionMapper.getQuestionsCountByInformationAndTypeAndDifficulty(information, type, difficulty);
                }
            }
        }
    }

    @Override
    public void updateQuestionByUid(QuestionEntity questionEntity) {
        questionMapper.updateQuestionByUid(questionEntity);
    }

    @Override
    public void addQuestions(List<QuestionEntity> questionEntityList) {
        for (QuestionEntity entity : questionEntityList)
            questionMapper.insertQuestion(entity);
    }

    @Override
    public void deleteQuestionsByUidList(List<Integer> uidList) {
        for (Integer uid : uidList) {
            questionMapper.deleteQuestionsByUid(uid);
        }
    }
}
