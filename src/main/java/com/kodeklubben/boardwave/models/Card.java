package com.kodeklubben.boardwave.models;

public class Card {

    private int id;
    private String title;
    private String description;
    private int minutesEstimated;
    private double hourlyRate;
    private int columnId;
    private int boardId;

    public Card() {
    }

    public Card(String title, String description, int minutesEstimated, double hourlyRate, int id) {
        this.title = title;
        this.description = description;
        this.minutesEstimated = minutesEstimated;
        this.hourlyRate = hourlyRate;
        this.id = id;
    }

    public Card(String title, String description, int minutesEstimated, double hourlyRate, int id, int columnId, int boardId) {
        this.title = title;
        this.description = description;
        this.minutesEstimated = minutesEstimated;
        this.hourlyRate = hourlyRate;
        this.id = id;
        this.columnId = columnId;
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", minutesEstimated=" + minutesEstimated +
                ", hourlyRate=" + hourlyRate +
                ", columnId=" + columnId +
                ", boardId=" + boardId +
                '}';
    }

    public int getColumnId() {
        return columnId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
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

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
