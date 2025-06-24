package com.sirius.controller;

import com.sirius.entity.QuestionEntity;
import com.sirius.utils.AIReplyParser;
import com.sirius.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deepseek")
public class DeepSeekController {
    @Resource
    private ChatModel chatModel;

    @GetMapping("/generateQuestionsByAI")
    public Result generateQuestions(@RequestParam String keyWord, @RequestParam Integer difficulty,
                                    @RequestParam String type, @RequestParam(defaultValue = "5") Integer number) {

        System.out.println("尝试生成关于" + keyWord + "的" + number + "道" + type + "题目，难度为" + difficulty + "。");

        String difficultyIndex = switch (difficulty) {
            case 1 -> "难度较低";
            case 2 -> "难度适中";
            case 3 -> "难度较高";
            default -> "难度随意";
        };
        String userText = """
                请帮我生成题目
                """;
        UserMessage userMessage = new UserMessage(userText);
        String systemText = switch (type) {
            case "single_choice" -> """
                    你是一个考试题目生成助手，你需要按照json格式生成关于{keyWord}的{difficultyIndex}的{number}个单选题，辅助出题人出题。
                    你只需要返回一个json格式的数组，除此之外不要做任何其他额外的回答。
                    json格式如下：
                        description: 题目描述,
                        optionsA: 选项A,
                        optionsB: 选项B,
                        optionsC: 选项C,
                        optionsD: 选项D,
                        answer: 答案,
                        explanation: 题目解析,
                    """;
            case "multiple_choice" -> """
                    你是一个考试题目生成助手，你需要按照json格式生成关于{keyWord}的{difficultyIndex}的{number}个多选题，辅助出题人出题。
                    你只需要返回一个json格式的数组，除此之外不要做任何其他额外的回答。
                    json格式如下：
                        description: 题目描述,
                        optionsA: 选项A,
                        optionsB: 选项B,
                        optionsC: 选项C,
                        optionsD: 选项D,
                        answer: 答案,
                        explanation: 题目解析,
                    其中关于多选题的答案你要用英文逗号隔开
                    """;
            case "true_false" -> """
                    你是一个考试题目生成助手，你需要按照json格式生成关于{keyWord}的{difficultyIndex}的{number}个判断题，辅助出题人出题。
                    你只需要返回一个json格式的数组，除此之外不要做任何其他额外的回答。
                    json格式如下：
                        description: 题目描述,
                        answer: 答案,
                        explanation: 题目解析,
                    其中answer的取值只能为true或false，true表示正确，false表示错误。
                    """;
            case "short_answer" -> """
                    你是一个考试题目生成助手，你需要按照json格式生成关于{keyWord}的{difficultyIndex}的{number}个简答题，辅助出题人出题。
                    你只需要返回一个json格式的数组，除此之外不要做任何其他额外的回答。
                    json格式如下：
                        description: 题目描述,
                        answer: 答案,
                        explanation: 题目解析,
                    """;
            default -> "";
        };
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate
                .createMessage(Map.of("keyWord", keyWord, "difficultyIndex", difficultyIndex, "number", number));
        Prompt prompt = new Prompt(systemMessage, userMessage);
        List<Generation> results = chatModel.call(prompt).getResults();
        // 流式编程
        String jsonQuestionArray = results.stream()
                .map(x -> x.getOutput().getContent())
                .collect(Collectors.joining(""))
                .replaceAll("^```json\\s*", "") // 移除开头的 ```json
                .replaceAll("\\s*```$", "");    // 移除结尾的 ```

        // 创建解析器对象
        AIReplyParser aiReplyParser = new AIReplyParser();
        // 解析以获得题目列表
        List<QuestionEntity> questionEntityList = switch (type) {
            case "single_choice" -> aiReplyParser.
                    parseSingleChoice(jsonQuestionArray, keyWord, difficulty);
            case "multiple_choice" -> aiReplyParser.
                    parseMultipleChoice(jsonQuestionArray, keyWord, difficulty);
            case "true_false" -> aiReplyParser.
                    parseTrueFalse(jsonQuestionArray, keyWord, difficulty);
            case "short_answer" -> aiReplyParser.
                    parseShortAnswer(jsonQuestionArray, keyWord, difficulty);
            default -> null;
        };
        System.out.println(questionEntityList);
        return Result.success(questionEntityList);
    }
}
