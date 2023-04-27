package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.services.DatabaseConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@org.springframework.stereotype.Repository
public class Repository {
    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("aws.connect.psdb.cloud", "8d0utw7evvzk45xh5rsz", "pscale_pw_rZU3nYQ2yfB3KNhq9xLTJdjeTs5m1qb4B5U9xCtREqv");

    private static final String GET_USER = "SELECT id, name, email, password FROM users WHERE id=?";

    private static final String GET_LATEST_USERID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String INSERT_NEW_USER = "INSERT INTO users(id, name, email, password) VALUES (?, ?, ?, ?)";

    private static final String GET_ID_FROM_LOGIN = "SELECT id FROM users WHERE email=? AND password=?";

    //private static final String GET_BOARDS = "SELECT id, name, userId  FROM board WHERE userId=?";

    User loginWithEmailAndPassword(String email, String password) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            User user;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));
            } else {
                user = new User("", "", "", -1);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int insertNewUser(String name, String email, String password) {
        int lastUserId = 0;
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LATEST_USERID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastUserId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement userInsertionStatement = dcm.getConnection().prepareStatement(INSERT_NEW_USER)) {
            userInsertionStatement.setInt(1, lastUserId + 1);
            userInsertionStatement.setString(2, name);
            userInsertionStatement.setString(3, email);
            userInsertionStatement.setString(4, password);
            userInsertionStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return lastUserId;
    }

    public int getIdFromAuthentication(String email, String password) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_ID_FROM_LOGIN)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            int id = -1;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public User getUser(int userId) {
        try(PreparedStatement statement = dcm.getConnection().prepareStatement(GET_USER)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User userModel = new User(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getInt("id"));
                return userModel;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*public ArrayList<Board> getWishLists(int userId) {
        try(PreparedStatement statement = dcm.getConnection().prepareStatement(GET_BOARDS)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Board> wishLists = new ArrayList<>();
            while (resultSet.next()) {
                wishLists.add(new Board(resultSet.getString("name"), getWishes(resultSet.getInt("id")), resultSet.getInt("id")));
            }
            return wishLists;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }*/

}
