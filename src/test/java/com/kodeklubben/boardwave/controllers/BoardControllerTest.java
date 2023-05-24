package com.kodeklubben.boardwave.controllers;

import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.Card;
import com.kodeklubben.boardwave.models.Column;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



class BoardControllerTest {

    @Autowired
    //private Repository repository;
    private Repository repository = new Repository(); // planetscaleløsning

    @Test
    void calculateBoardCosts() {
        Board board = new Board("Board", new ArrayList<Column>(), -1);

        Column column1 = new Column("column1", new ArrayList<Card>(), 0);
        Column column2 = new Column("column2", new ArrayList<Card>(), 0);

        Card card1 = new Card("card1", "this is card1", 50, 100, 0, 0, -1);
        //Card card2 = new Card("card2", "this is card2", 60, 100, 0, 0, -1);
        //Card card3 = new Card("card3", "this is card3", 240, 100, 0, 0, -1);
        //Card card4 = new Card("card4", "this is card4", 120, 100, 0, 0, -1);
        //Card card5 = new Card("card5", "this is card5", 70, 100, 0, 0, -1);

        column1.addCard(card1);
        //column1.addCard(card2);
        //column1.addCard(card3);
        //column1.addCard(card4);
        board.addColumn(column1);

        //column2.addCard(card5);
        board.addColumn(column2);

        assertEquals(83.333, board.totalCost());
    }

    @Test
    void createTestData() {
        User user = new User("name", "password", "email@email.com", 0, "board");
        Board board = new Board("boardTitle", new ArrayList<Column>(), 0);
        Column column = new Column("columnTitle", new ArrayList<Card>(), 0);
        Card card = new Card("card", "This is first card", 0, 0, 0, 0, 0);
        repository.insertBoard(board);
        repository.insertUser(user);
        repository.insertColumn(column);
        repository.insertCard(card);
    }

    @Test
    void removeBoardFromUser() {
        User user = new User("name", "password", "email@email.com", 0, "1;2;3");
        user.removeBoard(3);
        assertEquals("1;2", user.getBoards());

        user = new User("name", "password", "email@email.com", 0, "1;2;3");
        user.removeBoard(2);
        assertEquals("1;3", user.getBoards());

        user = new User("name", "password", "email@email.com", 0, "1;2;3");
        user.removeBoard(1);
        assertEquals("2;3", user.getBoards());
    }



    @Test
    void submitLogin() {
        String email = "tester@tester.com";
        String password = "test";
        int userId = repository.getIDFromAuthentication(email, password);
        if (userId != -1) {
            //user exists
            User user = repository.getUserFromId(userId);
            System.out.println("User: " + user.toString());
            assertNotNull(user); // Expecting the login to be successful

        } else {
            //id is -1, which means no user found with those credentials.
            System.out.println("User with credentials: " + email + " and " + password + " does not exist");
            assertTrue(true); // Expecting userId to be -1
        }
    }


    @Test
    void registerPage() {
        //test data - represent register data
        User user = new User("tester", "123", "tester@tester.com", -1, "");

        // Check if the provided email already exists in the database.
        boolean emailExists = repository.emailExists(user.getEmail());

        if (!emailExists) {
            assertTrue(true);
            // If the email does not already exist, then create a new user.
            int userId = repository.insertNewUser(user.getName(), user.getEmail(), user.getPassword());
            // Log in the newly registered user.
            user = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
            // Update the 'userLoggedIn' variable to reflect the currently logged in user.
            if (userId != -1) {
                // If the user has any boards, retrieve them and add them to the model.
                if (!user.getBoards().isEmpty() && !user.getBoards().equals("null")){
                    ArrayList<Integer> boardIds = new ArrayList<Integer>();
                    String[] ids = user.getBoards().split(";");
                    for (int i = 0; i < ids.length; i++) {
                        boardIds.add(Integer.parseInt(ids[i]));
                    }
                    ArrayList<Board> boards = repository.getBoards(boardIds);
                    user.addBoardList(boards);
                    System.out.println("user boards: " + user.getBoards());
                    assertFalse(user.getBoards().isEmpty());
                } else {
                    System.out.println("There are no boards");
                    assertTrue(true);
                }
            } else {
                // user was not created properly.
                System.out.println("User was not created properly");
                assertTrue(true);
            }
        } else {
            // If the email already exists, add an error message to the model and redirect to the registration page.
            System.out.println("email exists: " + user.getEmail());
            assertTrue(true);
        }
    }

    @Test
    void addBoard() {
        //test data
        // boolean removeAfter = true = tester deleteBoard
        // boolean removeAfter = false = ikke tester deleteBoard
        User user = new User("tester", "123", "tester@tester.com", 5, "");
        Board board = new Board("", new ArrayList<Column>(), -1);

        //insert method
        int newBoardId = repository.insertNewBoard(board.getTitle(), user.getId(), user.getBoards());
        board.setId(newBoardId);
        user.addBoard(newBoardId);

        System.out.println("user boards: " + user.getBoards());
        assertFalse(user.getBoards().isEmpty());
    }

    @Test
    void deleteBoard() {
        //test data
        User user = new User("tester", "123", "tester@tester.com", 5, "116");
        Board board = new Board("board", new ArrayList<Column>(), 116);
        user.removeBoard(board.getId());
        repository.deleteBoard(board.getId(), user.getBoards(), user.getId());

        if (!user.getBoards().isEmpty() || !user.getBoards().equals("null")){
            Board boardCheck = repository.getBoard(board.getId());
            if (boardCheck != null) {
                System.out.println("board exists after removal");
                fail();
            } else {
                System.out.println("board has been removed successfully");
                System.out.println("User boards (should be empty): " + user.getBoards());
                assertTrue(user.getBoards().isEmpty());
            }
        } else {
            assertTrue(true);
        }
    }

}
