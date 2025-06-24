package com.sirius.controller;

import com.sirius.entity.QuestionEntity;
import com.sirius.service.QuestionService;
import com.sirius.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    QuestionService questionService;

    @GetMapping("/getQuestionStats")
    public Result getQuestionStats() {
        return Result.success(questionService.getQuestionStats());
    }

    @GetMapping("/getQuestions")
    public Result getQuestions(@RequestParam Integer currentPageNumber, @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String information, @RequestParam(defaultValue = "") String type,
                               @RequestParam(defaultValue = "0") Integer difficulty) {
        return Result.success(questionService.getQuestions(currentPageNumber, pageSize, information, type, difficulty));
    }

    @GetMapping("/getQuestionsCount")
    public Result getQuestionsCount(@RequestParam(defaultValue = "") String information, @RequestParam(defaultValue = "") String type,
                                    @RequestParam(defaultValue = "0") Integer difficulty) {
        return Result.success(questionService.getQuestionsCount(information, type, difficulty));
    }

    @PutMapping("/updateQuestionByUid")
    public Result updateQuestionByUid(@RequestBody QuestionEntity questionEntity){
        questionService.updateQuestionByUid(questionEntity);
        return Result.success("update successfully");
    }

    @PostMapping("/addQuestions")
    public Result addQuestions(@RequestBody List<QuestionEntity> questionEntityList){
        questionService.addQuestions(questionEntityList);
        return Result.success("add successfully");
    }

    @DeleteMapping("/deleteQuestionsByUidList")
    public Result deleteQuestionsByUidList(@RequestBody List<Integer> uidList){
        if (!uidList.isEmpty()){
            questionService.deleteQuestionsByUidList(uidList);
            return Result.success("delete successfully");
        } else {
            return Result.error(400, "uidList is empty");
        }
    }

}
