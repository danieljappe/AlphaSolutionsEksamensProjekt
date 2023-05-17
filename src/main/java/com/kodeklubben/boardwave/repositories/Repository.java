package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.models.Column;
import com.kodeklubben.boardwave.models.Card;
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

    // Prepared Statements
    private static final String GET_LAST_USER_ID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String GET_LAST_BOARD_ID = "SELECT id FROM boards ORDER BY id DESC LIMIT 1";

    private static final String GET_USER = "SELECT id, name, email, password, boards FROM users WHERE email=? && password=?";
    private static final String GET_USER_FROM_LOGIN = "SELECT id FROM users WHERE email=? && password=?";
    private static final String GET_USER_FROM_ID = "SELECT id, name, password, email, boards FROM users WHERE id=?";

    private static final String UPDATE_USER_BOARDS = "UPDATE users SET boards=? WHERE id=?";


    private static final String GET_LATEST_USERID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String INSERT_NEW_USER = "INSERT INTO users(id, name, email, password, boards) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_USER_ID = "SELECT id, name, email, password, boards FROM users WHERE id=?";

    private static final String GET_BOARD = "SELECT id, name  FROM boards WHERE id=?";
    private static final String GET_LATEST_BOARD_ID = "SELECT id FROM boards ORDER BY id DESC LIMIT 1"; 
    private static final String INSERT_NEW_BOARD = "INSERT INTO boards(id, name) VALUES (?, ?)";

    private static final String GET_COLUMNS = "SELECT id, name FROM columns WHERE boardID=?";
    private static final String GET_CARDS = "SELECT id, title, description, minutesEstimated, hourlyRate, columnId FROM cards WHERE boardId=?";

/*     private static final String INSERT_NEW_CARD = "INSERT INTO card(id, title, description, minutesEstimated, hourlyRate, boardId, columnId) VALUES (?, ?, ?, ?, ?, ?, ?)";*/
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
                User user = new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"), resultSet.getString("boards"));
                
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
                user = new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"), resultSet.getString("boards"));
            } else {
                user = new User("", "", "", -1, "");
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
            userInsertionStatement.setString(5, "");
            userInsertionStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return lastUserId;
    }

    public int insertNewBoard(String name, int userId) {
        int lastBoardId = 0;
        //"SELECT id FROM boards ORDER BY id DESC LIMIT 1";
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
            userInsertionStatement.execute();
            System.out.println(userInsertionStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //#3 give user access to the board id
        if (lastBoardId != -1) {
            boards = boards + ";" + (lastBoardId + 1);
            if (boards.charAt(0) == ';') {
                boards = boards.substring(1, boards.length());
            }
            // update user data with new board id
            try(PreparedStatement updateStatement = dcm.getConnection().prepareStatement(UPDATE_USER_BOARDS)) {
                updateStatement.setString(1, boards);
                updateStatement.setInt(2, userId);
                updateStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
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
        }
    }


     public ArrayList<Column> getColumns(int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_COLUMNS)) {
            preparedStatement.setLong(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Column> columns = new ArrayList<Column>();
            while (resultSet.next()) {
                columns.add(new Column(resultSet.getString("name"), getCards(boardId), resultSet.getInt("id"))); 
            }
            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    public void editBoard(String name, int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(UPDATE_BOARD)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, boardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //delete board
    public void deleteBoard(int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement("DELETE FROM boards WHERE id=?")) {
            preparedStatement.setInt(1, boardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Board getBoard(int boardId) {
        //result list - what we return at the end
        Board result = new Board();

        //looping all ids in parameter list
        
            //Getting 1 board from id
            try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_BOARD)) {
                preparedStatement.setInt(1, boardId);
                ResultSet resultSet = preparedStatement.executeQuery();

                //getting columns from boardId
                ArrayList<Column> columns = getColumns(boardId);

                //adding board to result list
                while (resultSet.next()) {
                    result = (new Board(resultSet.getString("name"), columns, boardId));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        return result;
    }

}
