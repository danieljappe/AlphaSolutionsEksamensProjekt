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

    /*@Value("${PSCALE_USER}")

    private String pscaleUser;

    @Value("${PSCALE_USER_PASSWORD}")
    private String pscaleUserPassword;

    private final DatabaseConnectionManager dcm;

    public Repository(@Value("${PSCALE_USER}") String pscaleUser, @Value("${PSCALE_USER_PASSWORD}") String pscaleUserPassword) {
        this.dcm = new DatabaseConnectionManager("aws.connect.psdb.cloud", pscaleUser, pscaleUserPassword);
    }*/

    public Repository() {

    }
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("aws.connect.psdb.cloud", "yfik085is0kme8iquifq", "pscale_pw_UoXhWnaXJLXQVWAr6U8Jkwm3h1UWIS5opHc9fx30fJ4");



    // Prepared Statements
    private static final String GET_LAST_USER_ID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
    private static final String GET_LAST_BOARD_ID = "SELECT id FROM boards ORDER BY id DESC LIMIT 1";
    private static final String GET_LAST_CARD_ID = "SELECT id FROM cards ORDER BY id DESC LIMIT 1";
    private static final String GET_LAST_COLUMN_ID = "SELECT id FROM columns ORDER BY id DESC LIMIT 1";

    private static final String GET_USER = "SELECT id, name, email, password, boards FROM users WHERE email=? && password=?";
    private static final String GET_BOARD = "SELECT id, name  FROM boards WHERE id=?";
    private static final String GET_COLUMNS = "SELECT id, name FROM columns WHERE boardID=?";
    private static final String GET_CARDS = "SELECT id, title, description, minutesEstimated, hourlyRate, columnId FROM cards WHERE boardId=?";
    private static final String GET_CARDS_FROM_COLUMN_ID = "SELECT id, title, description, minutesEstimated, hourlyRate, columnId FROM cards WHERE boardId=? AND columnId=?";

    private static final String GET_USER_FROM_LOGIN = "SELECT id FROM users WHERE email=? && password=?";
    private static final String GET_USER_ID_FROM_EMAIL = "SELECT id FROM users WHERE email=?";
    private static final String GET_USER_FROM_ID = "SELECT id, name, password, email, boards FROM users WHERE id=?";

    private static final String UPDATE_USER_BOARDS = "UPDATE users SET boards=? WHERE id=?";
    private static final String UPDATE_BOARD = "UPDATE boards SET name=? WHERE id=?";
    private static final String UPDATE_CARD = "UPDATE title, description, minutesEstimated, hourlyRate, columnId SET title=?, description=?, minutesEstimated=?, hourlyRate=?, columnId? FROM cards WHERE id=?";
    private static final String UPDATE_COLUMN = "UPDATE Name SET Name=? FROM columns WHERE id=?";

    private static final String DELETE_BOARD = "DELETE FROM boards WHERE id=?";
    private static final String DELETE_CARD = "DELETE FROM cards WHERE id=?";
    private static final String DELETE_COLUMN = "DELETE FROM columns WHERE id=?";
    private static final String DELETE_CARDS_FROM_COLUMN_ID = "DELETE FROM cards WHERE columnId=?";
    private static final String DELETE_CARDS_FROM_BOARD_ID = "DELETE FROM cards WHERE boardId=?";
    private static final String DELETE_COLUMNS_FROM_BOARD_ID = "DELETE FROM columns WHERE boardId=?";

    private static final String INSERT_NEW_USER = "INSERT INTO users(id, name, email, password, boards) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_BOARD = "INSERT INTO boards(id, name) VALUES (?, ?)";
    private static final String INSERT_NEW_COLUMN = "INSERT INTO columns(id, name, boardId) VALUES (?, ?, ?)";
    private static final String INSERT_NEW_CARD = "INSERT INTO cards(id, title, description, minutesEstimated, hourlyRate, columnId, boardId) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String MOVE_CARD_TO_COLUMN = "UPDATE cards SET columnId=? WHERE id=?";


    //user data---------------------------------------------------------------------------------------------------------
    public int getIDFromAuthentication(String email, String password) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_FROM_LOGIN)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            //-1 = no result
            int id = -1;
            //check for results
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public User getUserFromId(int id) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_FROM_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getInt("id"),
                        resultSet.getString("boards")
                );
                return user;
            }
            //if nothing in result set, then null.
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
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_USER_ID_FROM_EMAIL)) {
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
        int lastUserId = -1;
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LAST_USER_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastUserId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //-1 = fail to get last used id from 'users'
        if (lastUserId != -1) {
            try (PreparedStatement userInsertionStatement = dcm.getConnection().prepareStatement(INSERT_NEW_USER)) {
                userInsertionStatement.setInt(1, lastUserId + 1);
                userInsertionStatement.setString(2, name);
                userInsertionStatement.setString(3, email);
                userInsertionStatement.setString(4, password);
                userInsertionStatement.setString(5, ""); //list of boards is empty in beginning
                userInsertionStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return lastUserId;
    }


    //board data--------------------------------------------------------------------------------------------------------
    public Board getBoard(int boardId) {
        Board result = null; //result list - to be returned at the end

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

    public ArrayList<Board> getBoards(ArrayList<Integer> ids) {
        ArrayList<Board> result = new ArrayList<Board>(); //result list - what we return at the end

        //looping all ids in parameter list
        for (int id: ids) {
            //Getting each board from id
            try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_BOARD)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                //getting columns from boardId
                ArrayList<Column> columns = getColumns(id);

                //adding board to result list
                while (resultSet.next()) {
                    result.add(new Board(resultSet.getString("name"), columns, id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public int insertNewBoard(String name, int userId, String boards) {
        int lastBoardId = -1;

        //#1 get latest board id, -1 = no result
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LAST_BOARD_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastBoardId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //#2 insert new board with a chosen name
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
        return lastBoardId + 1;
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

    public void deleteBoard(int boardId, String boards, int userId) {
        //delete board from boards table
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_BOARD)) {
            preparedStatement.setInt(1, boardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //delete board from user table
        try(PreparedStatement updateStatement = dcm.getConnection().prepareStatement(UPDATE_USER_BOARDS)) {
            updateStatement.setString(1, boards);
            updateStatement.setInt(2, userId);
            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //delete columns from column table
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_COLUMNS_FROM_BOARD_ID)) {
            preparedStatement.setInt(1, boardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //delete cards from card table
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_CARDS_FROM_BOARD_ID)) {
            preparedStatement.setInt(1, boardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //card data---------------------------------------------------------------------------------------------------------
    public ArrayList<Card> getCards(int boardId, int columnId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_CARDS_FROM_COLUMN_ID)) {
            //running query to get cards for a board id
            preparedStatement.setInt(1, boardId);
            preparedStatement.setInt(2, columnId);
            ResultSet resultSet = preparedStatement.executeQuery();

            //getting cards from query
            ArrayList<Card> cards = new ArrayList<Card>();
            while (resultSet.next()) {
                cards.add(new Card(resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("minutesEstimated"), resultSet.getFloat("hourlyRate"),  resultSet.getInt("id")));
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Card>();
        }
    }

    public int insertNewCard(String title, String description, int minutesEstimated, double hourlyRate, int columnId, int boardId) {
        int lastCardId = -1;

        //#1 get latest card id, -1 = no result
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LAST_CARD_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastCardId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //#2 insert new card with a chosen name
        try (PreparedStatement userInsertionStatement = dcm.getConnection().prepareStatement(INSERT_NEW_CARD)) {
            userInsertionStatement.setInt(1, lastCardId + 1);
            userInsertionStatement.setString(2, title);
            userInsertionStatement.setString(3, description);
            userInsertionStatement.setInt(4, minutesEstimated);
            userInsertionStatement.setDouble(5, hourlyRate);
            userInsertionStatement.setInt(6, columnId);
            userInsertionStatement.setInt(7, boardId);
            userInsertionStatement.execute();
            System.out.println(userInsertionStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return lastCardId + 1;
    }
    
    public void editCard(String title, String description, int minutesEstimated, double hourlyRate, int columnId, int cardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(UPDATE_CARD)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, minutesEstimated);
            preparedStatement.setDouble(4, hourlyRate);
            preparedStatement.setInt(5, columnId);
            preparedStatement.setInt(6, cardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteCard(int cardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_CARD)) {
            preparedStatement.setInt(1, cardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //column data-------------------------------------------------------------------------------------------------------
    public ArrayList<Column> getColumns(int boardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_COLUMNS)) {
            preparedStatement.setLong(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Column> columns = new ArrayList<Column>();
            while (resultSet.next()) {
                columns.add(new Column(resultSet.getString("name"), getCards(boardId, resultSet.getInt("id")), resultSet.getInt("id")));
            }
            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Column>();}
    }

    public int insertNewColumn(String name, int boardId) {
        int lastColumnId = -1;

        //#1 get latest column id, -1 = no result
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(GET_LAST_COLUMN_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lastColumnId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //#2 insert new column with a chosen name
        try (PreparedStatement userInsertionStatement = dcm.getConnection().prepareStatement(INSERT_NEW_COLUMN)) {
            userInsertionStatement.setInt(1, lastColumnId + 1);
            userInsertionStatement.setString(2, name);
            userInsertionStatement.setInt(3, boardId);
            userInsertionStatement.execute();
            System.out.println(userInsertionStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return lastColumnId + 1;
    }

    public void editColumn(String newName, int cardId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(UPDATE_COLUMN)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, cardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteColumn(int columnId) {
        // #1 Delete columns
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_COLUMN)) {
            preparedStatement.setInt(1, columnId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // #2 Delete cards in the column
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(DELETE_CARDS_FROM_COLUMN_ID)) {
            preparedStatement.setInt(1, columnId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //movecardtocolumn
    public void moveCardToColumn(int cardId, int columnId) {
        try (PreparedStatement preparedStatement = dcm.getConnection().prepareStatement(MOVE_CARD_TO_COLUMN)) {
            preparedStatement.setInt(1, columnId);
            preparedStatement.setInt(2, cardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
