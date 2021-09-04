package com.crud.demorest.controllers;

import com.crud.demorest.entitys.User;
import com.crud.demorest.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/admin")
    public String showUserList(@ModelAttribute("user") User user, Model model, Principal principal) {
        model.addAttribute("allUsers", userService.findAllUsers());
        model.addAttribute("allRoles", userService.findAllRoles());
        user = (User) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        model.addAttribute("user", user);
        return "admin";
    }
}
