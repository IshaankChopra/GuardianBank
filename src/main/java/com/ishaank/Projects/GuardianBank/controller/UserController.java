package com.ishaank.Projects.GuardianBank.controller;

import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/details")
    public String getUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User userInDB = userService.findByUserId(userId);
        return "Name: " + userInDB.getFirstName() + " " + userInDB.getLastName() + "\n"
                + "Email: " + userInDB.getEmail() + "\n"
                + "Phone: " + userInDB.getPhoneNumber() + "\n"
                + "Accounts: " + userInDB.getAccounts().stream()
                            .map(account -> "\n    " + account.getAccountType()
                                          + " - AccountNumber: " + account.getAccountNumber()
                                          + ", Balance: " + account.getBalance())
                            .collect(Collectors.joining("\n"));
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody User newUser) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.findByUserId(userId);

        user.setUserId(newUser.getUserId() != null && !newUser.getUserId().isEmpty() ?
                newUser.getUserId() : user.getUserId()
        );
        user.setFirstName(newUser.getFirstName() != null && !newUser.getFirstName().isEmpty() ?
                newUser.getFirstName() : user.getFirstName()
        );
        user.setLastName(newUser.getLastName() != null && !newUser.getLastName().isEmpty() ?
                newUser.getLastName() : user.getLastName()
        );
        user.setEmail(newUser.getEmail() != null && !newUser.getEmail().isEmpty() ?
                newUser.getEmail() : user.getEmail()
        );
        user.setPhoneNumber(newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty() ?
                newUser.getPhoneNumber() : user.getPhoneNumber()
        );
        userService.saveUser(user);
    }
}
