package com.kodeklubben.boardwave.models;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String boards;


    public User(String name, String password, String email, int id, String boards) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
        this.boards = boards;
    }

    public User() {
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
