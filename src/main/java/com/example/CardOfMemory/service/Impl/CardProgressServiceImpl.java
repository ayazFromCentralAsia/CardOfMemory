package com.example.CardOfMemory.service.Impl;

import com.example.CardOfMemory.dto.cardProgress.AnswerResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressByIdResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressRequest;
import com.example.CardOfMemory.dto.cardProgress.CardProgressResponse;
import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.CardProgress;
import com.example.CardOfMemory.entity.User;
import com.example.CardOfMemory.enums.Topic;
import com.example.CardOfMemory.exceptions.NotFoundException;
import com.example.CardOfMemory.repository.CardProgressRepository;
import com.example.CardOfMemory.repository.CardRepository;
import com.example.CardOfMemory.repository.UserRepository;
import com.example.CardOfMemory.service.CardProgressService;
import com.example.CardOfMemory.service.embedding.EmbeddingService;
import com.example.CardOfMemory.util.SimilarityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardProgressServiceImpl implements CardProgressService {

    private final CardProgressRepository cardProgressRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final EmbeddingService embeddingService;

    private static final double THRESHOLD = 0.8;

    @Override
    @Transactional
    public AnswerResponse answerCard(CardProgressRequest request) {
        log.info("Пользователь начал отвечать на карточку с id: {}", request.getCardId());

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new NotFoundException("Карточка не найдена"));

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication()
                        .getName()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Double> embUser    = embeddingService.getEmbedding(request.getAnswered());
        List<Double> embCorrect = embeddingService.getEmbedding(card.getAnswer());
        double similarity = SimilarityUtil.cosineSimilarity(embUser, embCorrect);
        boolean correct = similarity >= THRESHOLD;
        log.info("Similarity for card {}: {}", request.getAnswered(), similarity);

        CardProgress progress = CardProgress.builder()
                .card(card)
                .user(user)
                .answered(request.getAnswered())
                .score((int) Math.round(similarity))
                .known(correct)
                .createdAt(LocalDateTime.now())
                .build();
        cardProgressRepository.save(progress);

        return new AnswerResponse(
                card.getQuestion(),
                card.getAnswer(),
                request.getAnswered(),
                correct,
                similarity
        );
    }

    @Override
    public List<CardProgressByIdResponse> getCardProgress(UUID cardId) {
        log.info("Получение прогресса пользователей на карточке с id: {}", cardId);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Карточка не найдена"));

        List<CardProgress> progressList = card.getProgressList();

        return progressList.stream()
                .map(progress -> new CardProgressByIdResponse(
                        progress.getAnswered(),
                        progress.getKnown(),
                        progress.getScore(),
                        progress.getCreatedAt()
                )).toList();
    }

    @Override
    public List<CardProgressResponse> getAllCardProgress() {
        log.info("Получение прогресса пользователей на всех карточках");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        List<Card> cardList = cardProgressRepository.findCardsByUserId(user.getId());

        return cardList.stream()
               .flatMap(card -> card.getProgressList().stream())
               .map(progress -> new CardProgressResponse(
                        progress.getId(),
                        progress.getCard().getQuestion(),
                        progress.getCard().getDifficulty(),
                        progress.getCard().getTopic(),
                        progress.getAnswered(),
                        progress.getCard().getAnswer(),
                        progress.getKnown(),
                        progress.getScore(),
                        progress.getCreatedAt()
                )).toList();
    }

    @Override
    public void deleteCardProgress(UUID cardId) {
        log.info("Удаление прогресса пользователей на карточке с id: {}", cardId);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Карточка не найдена"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        cardProgressRepository.deleteByCardIdAndUserId(cardId, user.getId());
    }
}