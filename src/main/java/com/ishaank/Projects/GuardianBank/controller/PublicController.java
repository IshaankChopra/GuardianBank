package com.ishaank.Projects.GuardianBank.controller;

import com.ishaank.Projects.GuardianBank.dto.LoginRequest;
import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/ok")
    public String healthCheck() {
        return "OK!";
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.saveNewUser(user);
    }

//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest user) {
//
//    }

//    @GetMapping("/logout")
//    public String logout() {
//
//    }
}
