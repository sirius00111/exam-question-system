package com.sirius.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    private Integer uid;  // 题目ID，自增主键
    private String type;  // 题目类型，如 single_choice, multiple_choice, true_false, short_answer
    private String description;  // 题目描述（题干）
    private String options;  // JSON 格式选项，仅选择题有效
    private String answer;  // 正确答案，可能是字母、文字或多选组合
    private String explanation;  // 解析，可空
    private Integer difficulty;  // 难度等级（1-简单，2-中等，3-困难）
    private String tags;  // 标签字符串，如 "Java,基础"
    private LocalDateTime createdAt;  // 创建时间
    private LocalDateTime updatedAt;  // 更新时间
}
