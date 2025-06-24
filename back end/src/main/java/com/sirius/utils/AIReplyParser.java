package com.sirius.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sirius.entity.*;

import java.util.ArrayList;
import java.util.List;

// 自定义解析工具
public class AIReplyParser {
    // 创建Gson对象
    Gson gson = new Gson();

    // 解析ai返回的单选题json数组
    public List<QuestionEntity> parseSingleChoice(String jsonQuestionArray, String keyWord, Integer difficulty) {
        List<AISingleChoiceEntity> aiSingleChoiceEntityList = gson.fromJson(jsonQuestionArray,
                new TypeToken<List<AISingleChoiceEntity>>() {
                }.getType());
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        for (AISingleChoiceEntity entity : aiSingleChoiceEntityList) {
            String entityOptions = "{\"A\": \"" + entity.getOptionsA() +
                    "\", \"B\": \"" + entity.getOptionsB() +
                    "\", \"C\": \"" + entity.getOptionsC() +
                    "\", \"D\": \"" + entity.getOptionsD() + "\"}";
            questionEntityList.add(new QuestionEntity(null, "single_choice", entity.getDescription(), entityOptions,
                    entity.getAnswer(), entity.getExplanation(), difficulty, keyWord, null, null));
        }
        return questionEntityList;
    }

    // 解析ai返回的多选题json数组
    public List<QuestionEntity> parseMultipleChoice(String jsonQuestionArray, String keyWord, Integer difficulty) {
        List<AIMultipleChoiceEntity> aiMultipleChoiceEntityList = gson.fromJson(jsonQuestionArray,
                new TypeToken<List<AIMultipleChoiceEntity>>() {
                }.getType());
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        for (AIMultipleChoiceEntity entity : aiMultipleChoiceEntityList) {
            String entityOptions = "{\"A\": \"" + entity.getOptionsA() +
                    "\", \"B\": \"" + entity.getOptionsB() +
                    "\", \"C\": \"" + entity.getOptionsC() +
                    "\", \"D\": \"" + entity.getOptionsD() + "\"}";
            questionEntityList.add(new QuestionEntity(null, "multiple_choice", entity.getDescription(), entityOptions,
                    entity.getAnswer(), entity.getExplanation(), difficulty, keyWord, null, null));
        }
        return questionEntityList;
    }

    // 解析ai返回的判断题json数组
    public List<QuestionEntity> parseTrueFalse(String jsonQuestionArray, String keyWord, Integer difficulty) {
        List<AITrueFalseEntity> aiTrueFalseEntityList = gson.fromJson(jsonQuestionArray,
                new TypeToken<List<AITrueFalseEntity>>() {
                }.getType());
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        for (AITrueFalseEntity entity : aiTrueFalseEntityList) {
            questionEntityList.add(new QuestionEntity(null, "true_false", entity.getDescription(), null,
                    entity.getAnswer(), entity.getExplanation(), difficulty, keyWord, null, null));
        }
        return questionEntityList;
    }

    // 解析ai返回的简答题题json数组
    public List<QuestionEntity> parseShortAnswer(String jsonQuestionArray, String keyWord, Integer difficulty) {
        List<AIShortAnswer> aiShortAnswerList = gson.fromJson(jsonQuestionArray,
                new TypeToken<List<AIShortAnswer>>() {
                }.getType());
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        for (AIShortAnswer entity : aiShortAnswerList) {
            questionEntityList.add(new QuestionEntity(null, "short_answer", entity.getDescription(), null,
                    entity.getAnswer(), entity.getExplanation(), difficulty, keyWord, null, null));
        }
        return questionEntityList;
    }
}
