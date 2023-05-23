package com.kodeklubben.boardwave.models;

import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String boards;
    private ArrayList<Board> boardList;


    public User(String name, String password, String email, int id, String boards) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
        this.boards = boards;
        this.boardList = new ArrayList<>();
    }

    public User() {
    }

    public void addBoardList(ArrayList<Board> boards) {
        this.boardList.clear();
        this.boardList.addAll(boards);
    }

    public Board getBoardFromId(int id) {
        for (Board board: this.boardList) {
            if (board.getId() == id) {
                return board;
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

    public String getBoards() {
        if (this.boards == null) {
            return "";
        }
        return this.boards;
    }

    public void removeBoard(int id) {
        String[] boards = this.boards.split(";");
        int index = -1;
        for (int i = 0; i < boards.length; i++) {
            if (Integer.parseInt(boards[i]) == id) {
                index = i;
                break;
            }
        }
        //eksempel: 28;29;30;31;46;47;58;59
        if (boards.length == 1) {
            this.boards = "";
        } else if (index == 0) {
            this.boards.replaceFirst(id + ";", "");
        } else if (index > -1) {
            this.boards.replaceFirst(";" + id, "");
        }
    }

    public void addBoard(int id) {
        if (this.boards.isEmpty()) {
            this.boards = "" + id;
        } else {
            this.boards = this.boards + ";" + id;
        }
    }

    
    public void setBoards(String boards) {
        this.boards = boards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", boards='" + boards + '\'' +
                '}';
    }
}
