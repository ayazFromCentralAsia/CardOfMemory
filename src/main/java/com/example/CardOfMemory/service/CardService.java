package com.example.CardOfMemory.service;

import com.example.CardOfMemory.dto.card.CardRequest;
import com.example.CardOfMemory.dto.card.CardResponse;
import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.enums.Topic;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CardService {
    CardResponse createCard(CardRequest cardRequest);

    Page<Card> getCards(
            Topic topic,
            Integer difficulty,
            UUID owner,
            Boolean known,
            String search,
            int page,
            int size
    );

}
