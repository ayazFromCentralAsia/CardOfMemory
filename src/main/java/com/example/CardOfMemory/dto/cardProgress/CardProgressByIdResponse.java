package com.example.CardOfMemory.dto.cardProgress;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardProgressByIdResponse {
    private String userAnswer;
    private boolean known;
    private Integer score;
    private LocalDateTime createdAt;
}
