package com.kodeklubben.boardwave.controllers;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class BoardController {

    private User userLoggedIn;

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
    public String login(Model model) {

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
