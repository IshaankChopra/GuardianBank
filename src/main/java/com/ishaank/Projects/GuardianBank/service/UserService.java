package com.ishaank.Projects.GuardianBank.service;

import com.ishaank.Projects.GuardianBank.entity.Account;
import com.ishaank.Projects.GuardianBank.entity.AccountType;
import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.repository.AccountRepository;
import com.ishaank.Projects.GuardianBank.repository.AccountTypeRepository;
import com.ishaank.Projects.GuardianBank.repository.UserRepository;
import com.ishaank.Projects.GuardianBank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountUtil accountServiceUtil;
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Transactional
    public void saveNewUser(User user) {

        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        AccountType savingsAccountType = accountTypeRepository.findByType("Savings");

        Account account = new Account();
        account.setAccountType(savingsAccountType);
        account.setAccountNumber(accountServiceUtil.generateUniqueAccountNumber());
        account.setBalance(0.0d);
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(user);

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
