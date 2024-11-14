package com.ishaank.Projects.GuardianBank.bootstrap;

import com.ishaank.Projects.GuardianBank.entity.AccountType;
import com.ishaank.Projects.GuardianBank.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AccountTypesLoader implements CommandLineRunner {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public void run(String... args) throws Exception {

        List<String> accountTypes = Arrays.asList("Savings", "Current", "Fixed Deposit");
        for (String type : accountTypes) {
            if (accountTypeRepository.findByType(type) == null) {
                accountTypeRepository.save(new AccountType(null, type));
            }
        }
    }
}
