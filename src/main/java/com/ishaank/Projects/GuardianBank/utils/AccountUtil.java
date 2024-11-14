package com.ishaank.Projects.GuardianBank.utils;

import com.ishaank.Projects.GuardianBank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class AccountUtil {

    @Autowired
    private AccountRepository accountRepository;

    private static final int ACCOUNT_NUMBER_LENGTH = 8;
    private static final Random RANDOM = new Random();

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%08d", RANDOM.nextInt(100000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return "786-"+accountNumber;
    }
}
