package com.sirius.service;

import com.sirius.entity.QuestionEntity;
import com.sirius.entity.QuestionStatsEntity;

import java.util.List;

public interface QuestionService {
    Integer getQuestionsCount(String information, String type, Integer difficulty);
    List<QuestionEntity> getQuestions(Integer currentPageNumber, Integer pageSize, String information, String type, Integer difficulty);
    void updateQuestionByUid(QuestionEntity questionEntity);
    void addQuestions(List<QuestionEntity> questionEntityList);
    void deleteQuestionsByUidList(List<Integer> uidList);
    QuestionStatsEntity getQuestionStats();
}
