package com.kodeklubben.boardwave.models;

import java.util.ArrayList;

public class Board {
    int id;
    String title;
    ArrayList<Column> columns;

    public Board(String title, ArrayList<Column> columns, int id) {
        this.title = title;
        this.columns = columns;
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

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }
}
