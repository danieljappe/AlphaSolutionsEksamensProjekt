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
    private static final String GET_USER_FROM_LOGIN = "SELECT id FROM users WHERE email=? && password=?";

    private static final String GET_LATEST_USERID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String INSERT_NEW_USER = "INSERT INTO users(id, name, email, password) VALUES (?, ?, ?, ?)";

    private static final String GET_USER_ID = "SELECT id, name, email, password FROM users WHERE id=?";

    //TODO: Check if boards are correct
    private static final String GET_BOARD = "SELECT id, name  FROM boards WHERE id=?";
    private static final String GET_LATEST_BOARD_ID = "SELECT id FROM boards ORDER BY id DESC LIMIT 1"; 
    private static final String INSERT_NEW_BOARD = "INSERT INTO boards(id, name, userId) VALUES (?, ?, ?)";

    private static final String GET_COLUMNS = "SELECT id, name FROM columns WHERE boardID=?";
    private static final String GET_CARDS = "SELECT id, title, description, minutesEstimated, hourlyRate, columnId FROM cards WHERE boardId=?";

    //private static final String GET_BOARDS = "SELECT id, name, userId  FROM board WHERE userId=?";

    public int getIDFromAuthentication(String email, String password) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_FROM_LOGIN)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public User getUser(int id) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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

    public int insertNewBoard(String name, int userId) {
        int lastBoardId = 0;
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LATEST_BOARD_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastBoardId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement userInsertionStatement = dcm.getConnection().prepareStatement(INSERT_NEW_BOARD)) {
            userInsertionStatement.setInt(1, lastBoardId + 1);
            userInsertionStatement.setString(2, name);
            userInsertionStatement.setInt(3, userId);
            userInsertionStatement.execute();
            System.out.println(userInsertionStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return lastBoardId;
    }

    public ArrayList<Card> getCards(int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_CARDS)) {
            //running query
            preparedStatement.setInt(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            //getting cards from query
            ArrayList<Card> cards = new ArrayList<Card>();
            while (resultSet.next()) {
                cards.add(new Card(resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("minutesEstimated"), resultSet.getFloat("hourlyRate"),  resultSet.getInt("id")));
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            return new ArrayList<Card>();
        }
    }


     public ArrayList<Column> getColumns(int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_COLUMNS)) {
            preparedStatement.setLong(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Column> columns = new ArrayList<Column>();
            while (resultSet.next()) {
                columns.add(new Column(resultSet.getString("name"), getCards(boardId), resultSet.getString("id"))); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return columns;
    }

    public ArrayList<Board> getBoards(ArrayList<Integer> ids) {
        //result list - what we return at the end
        ArrayList<Board> result = new ArrayList<Board>();

        //looping all ids in parameter list
        for (int id: ids) {
            //Getting 1 board from id
            try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_BOARD)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                //getting columns from boardId
                ArrayList<Column> columns = getColumns(id);

                //adding board to result list
                while (resultSet.next()) {
                    result.add(resultSet.getString("name"), columns, id);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            preparedStatement.setInt(1, ids);
        }
        return result;
    }

    public ArrayList<Board> getBoardsOld(int userId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_BOARDS)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Board> boards = new ArrayList<>();
            while (resultSet.next()) {
                boards.add(new Board(resultSet.getString("name"), resultSet.getInt("id"), resultSet.getInt("userId")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return boards;
    }

}
