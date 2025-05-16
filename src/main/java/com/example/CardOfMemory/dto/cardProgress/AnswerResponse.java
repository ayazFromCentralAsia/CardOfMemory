package com.example.CardOfMemory.dto.cardProgress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private String question;
    private String answer;
    private String userAnswer;
    private boolean correct;
    private double similarity;
}
