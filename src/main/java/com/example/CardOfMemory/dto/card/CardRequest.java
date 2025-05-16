package com.example.CardOfMemory.dto.card;


import com.example.CardOfMemory.enums.Topic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Карта запроса")
public class CardRequest {
    @Schema(description = "Вопрос для карты", example = "What is OOP in Java?")
    @NotNull
    private String question;

    @Schema(description = "Ответ на вопрос", example = "Object-Oriented Programming is a " +
            "programming paradigm that focuses on the creation and manipulation of objects")
    @NotNull
    private String answer;

    @Schema(description = "Уровень сложности карты, 1-5", example = "1")
    @NotNull
    private int difficulty;

    @Schema(description = "Тема карты")
    @NotBlank
    private Topic topic;
}