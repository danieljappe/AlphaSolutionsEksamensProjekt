package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.services.DatabaseConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@org.springframework.stereotype.Repository
public class Repository {
    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("aws.connect.psdb.cloud", "8d0utw7evvzk45xh5rsz", "pscale_pw_rZU3nYQ2yfB3KNhq9xLTJdjeTs5m1qb4B5U9xCtREqv");

    private static final String GET_USER = "SELECT id, name, email, password FROM users WHERE id=?";

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
}
