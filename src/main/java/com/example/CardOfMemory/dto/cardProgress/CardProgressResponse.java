package com.example.CardOfMemory.dto.cardProgress;

import com.example.CardOfMemory.enums.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CardProgressResponse {
    private UUID cardId;
    private String question;
    private int difficulty;
    private Topic topic;
    private String userAnswer;
    private String correctAnswer;
    private boolean known;
    private Integer score;
    private LocalDateTime createdAt;
}
