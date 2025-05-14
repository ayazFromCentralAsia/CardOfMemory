package com.example.CardOfMemory.service.Impl;


import com.example.CardOfMemory.dto.card.CardRequest;
import com.example.CardOfMemory.dto.card.CardResponse;
import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.User;
import com.example.CardOfMemory.enums.Topic;
import com.example.CardOfMemory.filter.CardSpecification;
import com.example.CardOfMemory.repository.CardRepository;
import com.example.CardOfMemory.repository.UserRepository;
import com.example.CardOfMemory.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public CardResponse createCard(CardRequest cardRequest) {
        log.info("Создание новой карточки");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким email не найден"));

        Card newCard = Card.builder()
               .question(cardRequest.getQuestion())
               .answer(cardRequest.getAnswer())
               .difficulty(cardRequest.getDifficulty())
               .topic(cardRequest.getTopic())
               .owner(owner)
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .build();

        Card savedCard = cardRepository.save(newCard);

        log.info("Создана новая карточка с id: {}", savedCard.getId());

        return new CardResponse(
                savedCard.getId(),
                savedCard.getDifficulty(),
                savedCard.getTopic(),
                savedCard.getOwner().getId(),
                savedCard.getCreatedAt(),
                savedCard.getUpdatedAt());
    }

    public Page<Card> getCards(
            Topic topic,
            Integer difficulty,
            UUID owner,
            Boolean known,
            String search,
            int page,
            int size
    ) {
        log.info("Получение списка карточек");
        Pageable pageable = PageRequest.of(page, size);

        Specification<Card> spec = Specification
                .where(CardSpecification.hasTopic(topic))
                .and(CardSpecification.hasDifficulty(difficulty))
                .and(CardSpecification.hasOwner(owner))
                .and(CardSpecification.hasTextLike(search))
                .and(CardSpecification.knownByUser(known, owner));

        log.info("Сформирована спецификация: {}", spec);

        return cardRepository.findAll(spec, pageable);
    }
}