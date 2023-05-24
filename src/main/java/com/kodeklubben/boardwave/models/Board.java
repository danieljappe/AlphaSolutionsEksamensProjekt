package com.kodeklubben.boardwave.models;

import java.util.ArrayList;

public class Board {
    int id;
    String title;
    ArrayList<Column> columns;

    public float totalCost() {
        try {
            float totalCost = 0;
            for (Column column: this.columns) {
                totalCost += column.totalCost();
            }
            return totalCost;
        } catch (Exception e) {
            return 0;
        }
    }

    public int totalMinutes() {
        try {
            int totalMinutes = 0;
            for (Column column: this.columns) {
                totalMinutes += column.totalMinutes();
            }
            return totalMinutes;
        } catch (Exception e) {
            return 0;
        }
    }

    public float totalMinutesInHours() {
        return (float) this.totalMinutes() / 60;
    }


    public Board(){
    }

    public Board(String title, ArrayList<Column> columns, int id) {
        this.title = title;
        this.columns = columns;
        try {
            this.id = id;
        } catch (Exception e) {
            this.id = -1;
        }
    }

    public Column getColumnFromId(int id) {
        for (Column column: this.columns) {
            if (column.getId() == id) {
                return column;
            }
        }
        return null;
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

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public Column getColumnFromCardId(int cardId) {
        for (Column column: this.columns) {
            for (Card card: column.getCards()) {
                if (card.getId() == cardId) {
                    return column;
                }
            }
        }
        return null;
    }

    // Remove the column
    public void removeColumn(int columnId) {
        for (Column column: this.columns) {
            if (column.getId() == columnId) {
                this.columns.remove(column);
                break;
            }
        }
    }

    public Card getCardFromId(int cardId) {
        for (Column column: this.columns) {
            for (Card card: column.getCards()) {
                if (card.getId() == cardId) {
                    return card;
                }
            }
        }
        return null;
    }
}
