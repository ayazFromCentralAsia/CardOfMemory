package com.example.CardOfMemory.service.Impl;


import com.example.CardOfMemory.dto.card.CardRequest;
import com.example.CardOfMemory.dto.card.CardResponse;
import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.User;
import com.example.CardOfMemory.enums.Role;
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

    @Override
    public CardResponse getCard(UUID id) {
        log.info("Получение карточки с id: {}", id);

        Card card = cardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Карточка с таким id не найдена"));

        log.info("Найдена карточка: {}", card);

        return new CardResponse(
                card.getId(),
                card.getDifficulty(),
                card.getTopic(),
                card.getOwner().getId(),
                card.getCreatedAt(),
                card.getUpdatedAt());
    }

    @Override
    public void deleteCard(UUID id) {
        log.info("Удаление карточки с id: {}", id);

        Card card = cardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Карточка с таким id не найдена"));

        checkOwner(card);

        cardRepository.delete(card);

        log.info("Карточка удалена");
    }


    @Override
    public CardResponse editCard(UUID id, CardRequest cardRequest) {
        log.info("Редактирование карточки с id: {}", id);

        Card card = cardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Карточка с таким id не найдена"));

        checkOwner(card);

        card.setQuestion(cardRequest.getQuestion());
        card.setAnswer(cardRequest.getAnswer());
        card.setDifficulty(cardRequest.getDifficulty());
        card.setTopic(cardRequest.getTopic());
        card.setUpdatedAt(LocalDateTime.now());

        Card savedCard = cardRepository.save(card);

        log.info("Карточка успешно отредактирована");
        return new CardResponse(
                savedCard.getId(),
                savedCard.getDifficulty(),
                savedCard.getTopic(),
                savedCard.getOwner().getId(),
                savedCard.getCreatedAt(),
                savedCard.getUpdatedAt());
    }

    private void checkOwner(Card card) {
        User owner = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication()
                .getName()).orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден"));

        if (!card.getOwner().getId().equals(owner.getId()) && owner.getRole() == Role.USER) {
            throw new IllegalArgumentException("Нельзя удалять карточку, которой не принадлежит пользователь");
        } else if (owner.getRole() == Role.ADMIN) {
            log.info("Пользователь имеет право удалять карточку");
        }
    }
}