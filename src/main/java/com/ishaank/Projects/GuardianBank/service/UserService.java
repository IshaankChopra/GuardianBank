package com.ishaank.Projects.GuardianBank.service;

import com.ishaank.Projects.GuardianBank.entity.Account;
import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.enumeration.AccountType;
import com.ishaank.Projects.GuardianBank.repository.AccountRepository;
import com.ishaank.Projects.GuardianBank.repository.UserRepository;
import com.ishaank.Projects.GuardianBank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountUtil accountService;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Transactional
    public void saveNewUser(User user) {

        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        Account account = new Account();
        account.setAccountType(AccountType.SAVINGS);
        account.setAccountNumber(accountService.generateUniqueAccountNumber());
        account.setBalance(0.0d);
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(user);
//        Account newAccount = accountRepository.save(account);

        user.getAccounts().add(account);
        userRepository.save(user);

    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}
