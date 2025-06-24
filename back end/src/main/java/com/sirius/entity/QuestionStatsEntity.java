package com.sirius.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionStatsEntity {
    private Integer easyNumber;
    private Integer mediumNumber;
    private Integer hardNumber;
    private Integer singleChoiceNumber;
    private Integer multipleChoiceNumber;
    private Integer trueFalseNumber;
    private Integer shortAnswerNumber;
}
