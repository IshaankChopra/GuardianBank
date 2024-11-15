package com.ishaank.Projects.GuardianBank.service;

import com.ishaank.Projects.GuardianBank.entity.Account;
import com.ishaank.Projects.GuardianBank.entity.Transaction;
import com.ishaank.Projects.GuardianBank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> findByAccount(Account account) {
        return transactionRepository.findByAccountId(account);
    }
}
