package com.kodeklubben.boardwave.controllers;

import com.kodeklubben.boardwave.models.Card;
import com.kodeklubben.boardwave.models.Column;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class TrelloController {

    private List<Card> cards = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();

    @GetMapping("/trello-page")
    public String trelloPage(Model model) {
        model.addAttribute("cards", cards);
        model.addAttribute("card", new Card());
        return "trelloPage";
    }

    @PostMapping("/trello-page-add-new-card")
    public String trelloPageAddCard(@ModelAttribute("card") Card card, Model model) {
        cards.add(card);

        model.addAttribute("cards", cards);
        return "trelloPage";
    }
}
