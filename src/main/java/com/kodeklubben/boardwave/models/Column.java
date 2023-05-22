package com.kodeklubben.boardwave.models;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private int id;
    private String title;
    private ArrayList<Card> cards;
    private int boardId;

    public Column() {
    }

    public Column(String title, ArrayList<Card> cards, int id) {
        this.title = title;
        this.cards = cards;
        this.id = id;
    }

    public Column(String title, ArrayList<Card> cards, int id, int boardId) {
        this.title = title;
        this.cards = cards;
        this.id = id;
        this.boardId = boardId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void addCard(Card card) {
        this.cards.add(card);
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

    //remove card from column by card id
    public void deleteCard(int cardId) {
        try {
            for (Card card : this.cards) {
                if (card.getId() == cardId) {
                    this.cards.remove(card);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
