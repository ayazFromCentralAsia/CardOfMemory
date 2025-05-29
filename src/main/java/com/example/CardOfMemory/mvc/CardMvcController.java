package com.example.CardOfMemory.mvc;
import com.example.CardOfMemory.dto.card.CardRequest;
import com.example.CardOfMemory.enums.Topic;
import com.example.CardOfMemory.service.Impl.CardServiceImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardMvcController {

    private final CardServiceImpl cardService;

    // Список карт с фильтрами и пагинацией
    @GetMapping
    public String listCards(
            @RequestParam(required = false) Topic topic,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) UUID owner,
            @RequestParam(required = false) Boolean known,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        var cardsPage = cardService.getCards(topic, difficulty, owner, known, search, page, size);

        model.addAttribute("cardsPage", cardsPage);
        model.addAttribute("topic", topic);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("owner", owner);
        model.addAttribute("known", known);
        model.addAttribute("search", search);

        return "cards/list";
    }

    // Просмотр детали карты
    @GetMapping("/{id}")
    public String cardDetails(@PathVariable UUID id, Model model) {
        var card = cardService.getCard(id);
        model.addAttribute("card", card);
        return "cards/details";
    }

    // Форма создания новой карты — показать
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cardRequest", new CardRequest());
        return "cards/create";
    }

    // Обработка создания карты — POST
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/create")
    public String createCard(@Valid @ModelAttribute("cardRequest") CardRequest cardRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cards/create";
        }
        cardService.createCard(cardRequest);
        return "redirect:/cards";
    }

    // Форма редактирования карты — показать
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        var card = cardService.getCard(id);
        CardRequest cardRequest = new CardRequest();
        // Нужно маппить поля card -> cardRequest (зависит от твоей модели)
        // Например:
        cardRequest.setDifficulty(card.getDifficulty());
        cardRequest.setTopic(card.getTopic());

        // и другие поля
        model.addAttribute("cardRequest", cardRequest);
        model.addAttribute("cardId", id);
        return "cards/edit";
    }

    // Обработка редактирования карты — POST
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/{id}/edit")
    public String editCard(@PathVariable UUID id,
                           @Valid @ModelAttribute("cardRequest") CardRequest cardRequest,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cards/edit";
        }
        cardService.editCard(id, cardRequest);
        return "redirect:/cards/" + id;
    }

    // Удаление карты — POST (форму удаления можно сделать с кнопкой)
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return "redirect:/cards";
    }
}
