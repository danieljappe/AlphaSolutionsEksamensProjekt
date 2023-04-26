package com.kodeklubben.boardwave.models;

public class Card {
    private int id;
    private String title;
    private String description;
    private int minutesEstimated;
    private double hourlyRate;

    public Card(String title, String description, int minutesEstimated, double hourlyRate, int id) {
        this.title = title;
        this.description = description;
        this.minutesEstimated = minutesEstimated;
        this.hourlyRate = hourlyRate;
        this.id = id;
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
