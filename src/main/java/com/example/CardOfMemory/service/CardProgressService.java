package com.example.CardOfMemory.service;

import com.example.CardOfMemory.dto.cardProgress.*;
import com.example.CardOfMemory.dto.cardProgress.AnswerResponse;

import java.util.List;
import java.util.UUID;

public interface CardProgressService {
    AnswerResponse answerCard(CardProgressRequest cardProgress);

    List<CardProgressByIdResponse> getCardProgress(UUID cardId);

    List<CardProgressResponse> getAllCardProgress();

    void deleteCardProgress(UUID cardId);
}