package com.kodeklubben.boardwave.models;

import java.util.ArrayList;

public class Board {
    private int id;
    private String title;
    private ArrayList<String> whiteList;
    private ArrayList<Column> columns;

    public Board(String title, ArrayList<String> whiteList, ArrayList<Column> columns, int id) {
        this.title = title;
        this.whiteList = whiteList;
        this.columns = columns;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(ArrayList<String> whiteList) {
        this.whiteList = whiteList;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }
}
