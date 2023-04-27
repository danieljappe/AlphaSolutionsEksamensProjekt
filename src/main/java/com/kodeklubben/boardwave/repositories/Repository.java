package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.services.DatabaseConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@org.springframework.stereotype.Repository
public class Repository {
    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("eu-west.connect.psdb.cloud", "u5q2m8cezhxzprm9ez0j", "pscale_pw_Wn0MWYsTJiGmaRGehv77XNHM53t81JyumVrHZ4FQQlw");

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
