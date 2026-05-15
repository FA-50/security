package io.wjdtn747.formbased.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping
    public String showSignInPage(){
        return "singin";
    }
}
