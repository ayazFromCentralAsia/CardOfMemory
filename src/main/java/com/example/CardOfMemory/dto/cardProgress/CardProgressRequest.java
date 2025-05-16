package com.example.CardOfMemory.dto.cardProgress;

import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Карточка прогресса пользователя, запрос")
public class CardProgressRequest {
    @Schema(description = "Идентификатор карточки")
    private UUID cardId;

    @Schema(description = "Ответ на вопрос")
    private String answered;
}