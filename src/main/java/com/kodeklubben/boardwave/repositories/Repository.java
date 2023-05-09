package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.services.DatabaseConnectionManager;
import org.springframework.beans.factory.annotation.Value;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@org.springframework.stereotype.Repository
public class Repository {

    @Value("${PSCALE_USER}")
    private String pscaleUser;

    @Value("${PSCALE_USER_PASSWORD}")
    private String pscaleUserPassword;

    private final DatabaseConnectionManager dcm;

    public Repository(@Value("${PSCALE_USER}") String pscaleUser, @Value("${PSCALE_USER_PASSWORD}") String pscaleUserPassword) {
    this.dcm = new DatabaseConnectionManager("aws.connect.psdb.cloud", pscaleUser, pscaleUserPassword);
    }


    

    private static final String GET_USER = "SELECT id, name, email, password FROM users WHERE email=? && password=?";


    private static final String GET_LATEST_USERID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String INSERT_NEW_USER = "INSERT INTO users(id, name, email, password) VALUES (?, ?, ?, ?)";

    private static final String GET_USER_ID = "SELECT id FROM users WHERE email=?";

    //private static final String GET_BOARDS = "SELECT id, name, userId  FROM board WHERE userId=?";

    public User loginWithEmailAndPassword(String email, String password) {
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

    public boolean emailExists(String email) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_ID)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id != -1;
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

}
