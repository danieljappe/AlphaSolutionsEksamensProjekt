package com.kodeklubben.boardwave.controllers;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class BoardController {

    private User userLoggedIn;
    User user = new User();

    @Autowired
    private Repository repository;

    // Landing page
    @GetMapping("/")
    public String landingPage(){
        return "index";
    }

    // Login
    @GetMapping("/login-page")
    public String loginPage(Model model) {
        User userTemplate = new User("", "", "", -1, "");
        model.addAttribute("userTemplate", userTemplate);
        return "loginPage";
    }

    @GetMapping("/credentials")
    public Boolean submitLogin(@RequestParam String id, Model model){
        String email = id.split(";")[0];
        String password = id.split(";")[1];
        int userID = repository.getIDFromAuthentication(email, password);
        if (userID != -1){
            user = repository.getUser(userID);
            userLoggedIn = user;
            //add users board to model
            model.addAttribute("user", user);
            System.out.println(user.toString()); //works
            return true;
        }
        return false;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        boolean success = submitLogin(user.getEmail() + ";" + user.getPassword(), model);
        if (success) {
            //user exists and found
            //add users boards to model

            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            System.out.println("user:");
            System.out.println(userLoggedIn);
            String[] ids = userLoggedIn.getBoards().split(";");
            for (int i = 0; i < ids.length; i++) {
                boardIds.add(Integer.parseInt(ids[i]));
            }
        
            ArrayList<Board> boards = repository.getBoards(boardIds);
            model.addAttribute("boards", boards);
            return "userHomePage";
        } else {
            model.addAttribute("error", true);
            return loginPage(model);
        }
    }

    // Register
    @GetMapping("/register-page")
    public String registerPage(Model model) {
        User userTemplate = new User("", "", "", -1, "");
        model.addAttribute("userTemplate", userTemplate);
        return "registerPage";
    }

    @PostMapping("/register-page")
    public String createUser(@ModelAttribute("user") User user, Model model){
        System.out.println(user.toString());
        boolean emailExists = repository.emailExists(user.getEmail());
        if (!emailExists) {
            repository.insertNewUser(user.getName(), user.getEmail(), user.getPassword());
            user = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
            System.out.println(user);
            if (user.getId() != -1) {
                //user exists and found
                return "userHomePage";
            } else {
                model.addAttribute("error", true);
                return registerPage(model);
            }
        } else {
            model.addAttribute("EmailError", true);
            return registerPage(model);
        }
    }

    @PostMapping("/userHomePage")
    public String submitCreateBoard(@ModelAttribute("board") Board board, Model model){
        int userID = repository.getIDFromAuthentication(user.getEmail(), user.getPassword());
        repository.insertNewBoard(board.getTitle(), userID);
        ArrayList<Integer> boardIds = new ArrayList<Integer>();
        String[] ids = user.getBoards().split(";");
        for (int i = 0; i < ids.length; i++) {
            boardIds.add(Integer.parseInt(ids[i]));
        }
        ArrayList<Board> boards = repository.getBoards(boardIds);
        model.addAttribute("boards", boards);
        return "userHomePage";
    }


    // Contact
    @GetMapping("/contact-page")
    public String contactPage() {
        return "contactPage";
    }

    // Contact
    @GetMapping("/about-page")
    public String aboutPage() {
        return "aboutPage";
    }

    @GetMapping("/error-page")
    public String errorPage() {
        return "errorPage";
    }

    @GetMapping("/error")
    public String error() {
        return errorPage();
    }

    @PostMapping("/boards")
    public String createBoard() {

        return "redirect:/userHomePage";
    }

    @GetMapping("/dontPress")
    public String dontPress(){
        return "dontPress";
    }
}

