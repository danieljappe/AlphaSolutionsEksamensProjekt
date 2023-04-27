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
        //TODO valider email og password

        return "userHomePage";
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
        repository.insertNewUser(user.getName(), user.getEmail(), user.getPassword());
        submitLogin(user.getEmail() + ";" + user.getPassword(), model);
        return "";
    }

    //login with email and password
    @GetMapping("/credentials")
    public String submitLogin(@RequestParam String id, Model model, boolean isLogin) {
        //http://localhost:8080/credentials?id=frederikbehrens90@gmail.com;123
        String email = id.split(";")[0];
        String password = id.split(";")[1];
        int userId = repository.getIdFromAuthentication(email, password);
        if (userId != -1) {
            user = repository.getUser(userId);
            ArrayList<Board> wishLists = repository.getWishLists(userId);
            model.addAttribute("userModel", userModel);
            System.out.println(userModel.getName());
            model.addAttribute("wishLists", wishLists);
            System.out.println(wishLists.size());
            return "profile";
        } else {
            if (isLogin) {
                return "login";
            } else {
                return "register";
            }
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
