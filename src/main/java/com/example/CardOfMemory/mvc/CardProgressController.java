package com.example.CardOfMemory.mvc;
import com.example.CardOfMemory.dto.cardProgress.AnswerResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressByIdResponse;
import com.example.CardOfMemory.dto.cardProgress.CardProgressRequest;
import com.example.CardOfMemory.dto.cardProgress.CardProgressResponse;
import com.example.CardOfMemory.service.CardProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/card-progress")
@RequiredArgsConstructor
public class CardProgressController {

    private final CardProgressService cardProgressService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/answer")
    public String answerCard(@Validated @ModelAttribute CardProgressRequest cardProgress, Model model) {
        AnswerResponse response = cardProgressService.answerCard(cardProgress);
        model.addAttribute("response", response);
        return "card/answer-result";
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{cardId}")
    public String getCardProgress(@PathVariable("cardId") UUID cardId, Model model) {
        List<CardProgressByIdResponse> progress = cardProgressService.getCardProgress(cardId);
        model.addAttribute("progress", progress);
        return "card/progress-by-id";
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/all")
    public String getAllCardProgress(Model model) {
        List<CardProgressResponse> allProgress = cardProgressService.getAllCardProgress();
        model.addAttribute("allProgress", allProgress);
        return "card/all-progress";
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/delete/{cardId}")
    public String deleteCardProgress(@PathVariable("cardId") UUID cardId) {
        cardProgressService.deleteCardProgress(cardId);
        return "redirect:/card-progress/all";
    }
}
