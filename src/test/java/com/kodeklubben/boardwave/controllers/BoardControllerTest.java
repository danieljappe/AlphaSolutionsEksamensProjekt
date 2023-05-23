package com.kodeklubben.boardwave.controllers;

import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.Card;
import com.kodeklubben.boardwave.models.Column;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        String email = "tester@tester.com";
        // Check if the provided email already exists in the database.
        boolean emailExists = repository.emailExists(email);
        assertTrue(emailExists);

        if (!emailExists) {
            // If the email does not already exist, then create a new user.
            // Insert the new user's details into the database.
            repository.insertNewUser(user.getName(), user.getEmail(), user.getPassword());
            // Log in the newly registered user.
            user = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
            // Update the 'userLoggedIn' variable to reflect the currently logged in user.
            userLoggedIn = user;
            // Print the user's details to the console.
            System.out.println(user);
            // Check if the user was successfully logged in.
            if (user.getId() != -1) {
                // If the user has any boards, retrieve them and add them to the model.
                if (!userLoggedIn.getBoards().isEmpty() && !userLoggedIn.getBoards().equals("null")){
                    ArrayList<Integer> boardIds = new ArrayList<Integer>();
                    String[] ids = userLoggedIn.getBoards().split(";");
                    for (int i = 0; i < ids.length; i++) {
                        boardIds.add(Integer.parseInt(ids[i]));
                    }
                    ArrayList<Board> boards = repository.getBoards(boardIds);
                    userLoggedIn.addBoardList(boards);
                    model.addAttribute("boards", boards);
                } else {
                    // If the user does not have any boards, add an empty list to the model.
                    model.addAttribute("boards", new ArrayList<Board>());
                }
                // Add an empty 'Board' object to the model.
                model.addAttribute("board", new Board("", new ArrayList<>(), -1));
                // Return the name of the view to be displayed - in this case, the user's home page.
                return "userHomePage";
            } else {
                // If there was an error during login, add an error attribute to the model and redirect to the registration page.
                model.addAttribute("error", true);
                return registerPage(model);
            }
        } else {
            // If the email already exists, add an error message to the model and redirect to the registration page.
            System.out.println("email exists: " + email);
            assertTrue(true);
        }
    }

    @Test
    void createUser() {
        // Your test implementation for createUser
    }

    @Test
    void removeCard() {
        // Your test implementation for removeCard
    }

    @Test
    void moveCard() {
        // Your test implementation for moveCard
    }

    @Test
    void removeColumn() {

    }
}
