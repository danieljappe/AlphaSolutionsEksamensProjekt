package com.kodeklubben.boardwave.controllers;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class BoardController {

    private User userLoggedIn;
    User user = new User();

    private final Repository repository = new Repository();

    //TODO mangler userTemplate object

    // Landing page
    @GetMapping("/")
    public String landingPage(){
        return "index";
    }

    // Login
    @GetMapping("/login-page")
    public String loginPage(Model model) {
        User userTemplate = new User("", "", "", -1);
        model.addAttribute("userTemplate", userTemplate);
        return "loginPage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        user = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
        System.out.println(user);
        if (user.getId() != -1) {
            //user exists and found
            return "userHomePage";
        } else {
            model.addAttribute("error", true);
            return loginPage(model);
        }
    }

    // Register
    @GetMapping("/register-page")
    public String registerPage(Model model) {
        User userTemplate = new User("", "", "", -1);
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

    @GetMapping("/trello-page")
    public String trelloPage() {
        return "trelloPage";
    }

    @GetMapping("/error-page")
    public String errorPage() {
        return "errorPage";
    }

    @GetMapping("/error")
    public String error() {
        return errorPage();
    }
}
