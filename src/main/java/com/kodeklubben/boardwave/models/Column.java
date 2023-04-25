package com.kodeklubben.boardwave.models;

import java.util.ArrayList;

public class Column {
    private String title;
    private ArrayList<Card> cards;

    public Column(String title, ArrayList<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
