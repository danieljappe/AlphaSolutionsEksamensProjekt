package com.kodeklubben.boardwave.controllers;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

    //Landing page
    @GetMapping("/")
    public String landingPage(){
        return "index";
    }


}
