package com.example.CardOfMemory.api;


import com.example.CardOfMemory.dto.auth.AuthenticationResponse;
import com.example.CardOfMemory.dto.auth.SignUpRequest;
import com.example.CardOfMemory.dto.card.CardRequest;
import com.example.CardOfMemory.dto.card.CardResponse;
import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.enums.Topic;
import com.example.CardOfMemory.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/card")
@Tag(name = "Сервис работы с картами")
@RequiredArgsConstructor
public class CardApi {
    private final CardService cardService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/create")
    @Operation(
            summary = "Создание новой карты",
            description = "Создание новой карты с предоставленными данными. "
    )
    public CardResponse createCard(@Validated @RequestBody CardRequest cardRequest){
        return cardService.createCard(cardRequest);
    }




    @GetMapping
    public Page<Card> getCards(
            @RequestParam(required = false) Topic topic,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) UUID owner,
            @RequestParam(required = false) Boolean known,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return cardService.getCards(topic, difficulty, owner, known, search, page, size);
    }

}