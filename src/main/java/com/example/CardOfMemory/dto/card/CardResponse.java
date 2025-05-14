package com.example.CardOfMemory.dto.card;

import com.example.CardOfMemory.entity.User;
import com.example.CardOfMemory.enums.Topic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private UUID id;
    private int difficulty;
    private Topic topic;
    private UUID owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
