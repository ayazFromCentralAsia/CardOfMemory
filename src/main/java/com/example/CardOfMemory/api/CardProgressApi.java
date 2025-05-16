package com.example.CardOfMemory.api;



import com.example.CardOfMemory.dto.cardProgress.AnswerResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressByIdResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressRequest;
import com.example.CardOfMemory.dto.cardProgress.CardProgressResponse;
import com.example.CardOfMemory.service.CardProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/card-progress")
@Tag(name = "Сервис для управления прогрессом карты для пользователя")
@RequiredArgsConstructor
public class CardProgressApi {

    private final CardProgressService cardProgressService;

    @Operation(
            summary = "Ендпоинт для ответа на карточки",
            description = "Пользователь отвечает на карточку, передает ответ и получает результат. " +
                    "Ответ обрабатывается ии сохраняется в БД."
    )
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/answer")
    public AnswerResponse createCard(@Validated @RequestBody CardProgressRequest cardProgress){
        return cardProgressService.answerCard(cardProgress);
    }

    @Operation(
            summary = "Ендпоинт для получения прогресса карты",
            description = "Пользователь получает прогресс карты, которая показывает его уровень прогресса. "
    )
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/{cardId}")
    public List<CardProgressByIdResponse> getCardProgress(@PathVariable("cardId") UUID cardId) {
        return cardProgressService.getCardProgress(cardId);
    }

    @Operation(
            summary = "Ендпоинт для получения прогресса всех карт",
            description = "Пользователь получает прогресс всех карт, которая показывает его уровень прогресса. "
    )
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/all")
    public List<CardProgressResponse> getAllCardProgress() {
        return cardProgressService.getAllCardProgress();
    }

    @Operation(
            summary = "Ендпоинт для удаления прогресса карты",
            description = "Пользователь может удалить весь прогресс карты, если он не хочет продолжать изучать. "
    )
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/{cardId}")
    public void deleteCardProgress(@PathVariable("cardId") UUID cardId) {
        cardProgressService.deleteCardProgress(cardId);
    }
}
