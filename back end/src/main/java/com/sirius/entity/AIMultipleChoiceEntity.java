package com.sirius.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AIMultipleChoiceEntity {
    private String description;
    private String optionsA;
    private String optionsB;
    private String optionsC;
    private String optionsD;
    private String answer;
    private String explanation;
}
