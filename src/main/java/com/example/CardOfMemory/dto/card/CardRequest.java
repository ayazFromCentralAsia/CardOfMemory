package com.example.CardOfMemory.dto.card;


import com.example.CardOfMemory.enums.Topic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Card request object")
public class CardRequest {
    @Schema(description = "Question of the card", example = "What is OOP in Java?")
    @NotNull
    private String question;

    @Schema(description = "Answer of the card", example = "Object-Oriented Programming is a " +
            "programming paradigm that focuses on the creation and manipulation of objects")
    @NotNull
    private String answer;

    @Schema(description = "Difficulty level of the card, 1-5", example = "1")
    @NotNull
    private int difficulty;

    @Schema(description = "Topic of the card")
    @NotBlank
    private Topic topic;
}