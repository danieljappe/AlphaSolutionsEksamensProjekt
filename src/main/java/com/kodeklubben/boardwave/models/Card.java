package com.kodeklubben.boardwave.models;

public class Card {
    private String title;
    private String description;
    private int minutesEstimated;

    public Card(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinutesEstimated() {
        return minutesEstimated;
    }

    public void setMinutesEstimated(int minutesEstimated) {
        this.minutesEstimated = minutesEstimated;
    }
}
