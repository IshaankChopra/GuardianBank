package com.ishaank.Projects.GuardianBank.controller;

import com.ishaank.Projects.GuardianBank.dto.AccountRequest;
import com.ishaank.Projects.GuardianBank.entity.Account;
import com.ishaank.Projects.GuardianBank.entity.AccountType;
import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.repository.AccountTypeRepository;
import com.ishaank.Projects.GuardianBank.service.UserService;
import com.ishaank.Projects.GuardianBank.utils.AccountTypeUtil;
import com.ishaank.Projects.GuardianBank.utils.AccountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountTypeRepository accountTypeRepository;
    @Autowired
    private AccountUtil accountServiceUtil;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info(userId);

        User userInDB = userService.findByUserId(userId);
        String details = "Name: " + userInDB.getFirstName() + " " + userInDB.getLastName() + "\n"
                + "Email: " + userInDB.getEmail() + "\n"
                + "Phone: " + userInDB.getPhoneNumber() + "\n"
                + "Accounts: \n" + userInDB.getAccounts().stream()
                .map(account -> "    " + account.getAccountType().getType())
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User newUser) {

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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.findByUserId(userId);
        StringBuilder response = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Account account : user.getAccounts()) {
            response.append(" Account Type: ").append(account.getAccountType().getType()).append("\n");
            response.append(" Account Number: ").append(account.getAccountNumber()).append("\n");
            response.append(" Balance: ").append(account.getBalance()).append("\n");
            response.append(" Created At: ").append(account.getCreatedAt().format(dateFormatter)).append("\n\n");
        }
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-account")
    public ResponseEntity<?> addAccount(@RequestBody AccountRequest account) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.findByUserId(userId);

        AccountType accountType = accountTypeRepository.findByType(account.type());
        if (accountType == null) {
            return new ResponseEntity<>("Incorrect Account type!", HttpStatus.BAD_REQUEST);
        }
        List<AccountType> collect = user.getAccounts().stream().filter(x -> x.getAccountType().getType().equals(account.type()))
                .map(Account::getAccountType)
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return new ResponseEntity<>("Account already exists!", HttpStatus.BAD_REQUEST);
        }

        Account newAccount = new Account();
        newAccount.setAccountType(accountType);
        newAccount.setAccountNumber(accountServiceUtil.generateUniqueAccountNumber());
        newAccount.setBalance(0.0d);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setUser(user);

        user.getAccounts().add(newAccount);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
