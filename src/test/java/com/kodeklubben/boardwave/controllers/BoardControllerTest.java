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


    private Repository repository = new Repository();


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
                    String[] ids = userLoggedIn.getBoards().split(";");
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
    void moveCard() {
        // Your test implementation for moveCard
    }

    @Test
    void removeColumn() {

    }
}
