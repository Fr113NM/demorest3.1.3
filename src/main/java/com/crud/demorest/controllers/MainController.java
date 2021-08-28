package com.crud.demorest.controllers;

import com.crud.demorest.entitys.User;
import com.crud.demorest.services.UserServiceImpl;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public MainController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String showUserPage(Model model, Principal principal) {
        model.addAttribute("user", userServiceImpl.findByUsername(principal.getName()));
//        model.addAttribute("user", User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/admin")
    public String showUserList(@ModelAttribute("user") User user, Model model, Principal principal) {
        model.addAttribute("allUsers", userServiceImpl.findAllUsers());
        model.addAttribute("allRoles", userServiceImpl.findAllRoles());
        model.addAttribute("user", userServiceImpl.findByUsername(principal.getName()));

        return "admin";
    }
}
