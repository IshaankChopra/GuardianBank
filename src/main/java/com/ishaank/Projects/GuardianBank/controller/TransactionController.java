package com.ishaank.Projects.GuardianBank.controller;

import com.ishaank.Projects.GuardianBank.dto.AccountNoRequest;
import com.ishaank.Projects.GuardianBank.entity.Account;
import com.ishaank.Projects.GuardianBank.entity.Transaction;
import com.ishaank.Projects.GuardianBank.service.AccountService;
import com.ishaank.Projects.GuardianBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/payment")
    public ResponseEntity<String> makePayment(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Account accountFrom = accountService.findByAccountNumber(transaction.getAccountFrom());
        if (!accountFrom.getUser().getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        try {
            Account accountTo = accountService.findByAccountNumber(transaction.getAccountNo());
            if (accountFrom.getBalance() < transaction.getAmount()) {
                transaction.setTransactionResult("Failed");
                transaction.setAccountId(accountFrom);
                transaction.setTransactionType("Payment");
                transactionService.saveTransaction(transaction);
                return new ResponseEntity<>("Not enough balance.", HttpStatus.BAD_REQUEST);
            }
            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
            accountService.saveAccount(accountFrom);
            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountService.saveAccount(accountTo);
            transaction.setTransactionResult("Successful");
            transaction.setAccountId(accountFrom);
            transaction.setTransactionType("Payment");
            transactionService.saveTransaction(transaction);
            Transaction depositTransaction = new Transaction();
            depositTransaction.setBeneficiary(null);
            depositTransaction.setAccountFrom(transaction.getAccountFrom());
            depositTransaction.setAccountNo(null);
            depositTransaction.setAmount(transaction.getAmount());
            depositTransaction.setReference(transaction.getReference());
            depositTransaction.setTransactionResult("Successful");
            depositTransaction.setTransactionType("Deposit");
            depositTransaction.setAccountId(accountTo);
            transactionService.saveTransaction(depositTransaction);
            return new ResponseEntity<>("Payment successful.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing payment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Account accountFrom = accountService.findByAccountNumber(transaction.getAccountFrom());
        if (!accountFrom.getUser().getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        try {
            Account accountTo = accountService.findByAccountNumber(transaction.getAccountTo());
            if (accountFrom.getBalance() < transaction.getAmount()) {
                transaction.setTransactionResult("Failed");
                transaction.setAccountId(accountFrom);
                transaction.setTransactionType("Transfer");
                transactionService.saveTransaction(transaction);
                return new ResponseEntity<>("Not enough balance.", HttpStatus.BAD_REQUEST);
            }
            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountService.saveAccount(accountFrom);
            accountService.saveAccount(accountTo);
            transaction.setTransactionResult("Successful");
            transaction.setAccountId(accountFrom);
            transaction.setTransactionType("Transfer");
            transactionService.saveTransaction(transaction);
            Transaction reverseTransaction = new Transaction();
            reverseTransaction.setBeneficiary(transaction.getBeneficiary());
            reverseTransaction.setAccountFrom(transaction.getAccountTo());
            reverseTransaction.setAccountTo(transaction.getAccountFrom());
            reverseTransaction.setAmount(transaction.getAmount());
            reverseTransaction.setReference(transaction.getReference());
            reverseTransaction.setTransactionResult("Successful");
            reverseTransaction.setAccountId(accountTo);
            reverseTransaction.setTransactionType("Deposit");
            transactionService.saveTransaction(reverseTransaction);
            return new ResponseEntity<>("Transfer successful.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing transfer.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Account accountFrom = accountService.findByAccountNumber(transaction.getAccountFrom());
        if (!accountFrom.getUser().getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (accountFrom.getBalance() < transaction.getAmount()) {
                transaction.setTransactionResult("Failed");
                transaction.setAccountId(accountFrom);
                transaction.setTransactionType("Withdraw");
                transactionService.saveTransaction(transaction);
                return new ResponseEntity<>("Not enough balance.", HttpStatus.BAD_REQUEST);
            }
            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
            accountService.saveAccount(accountFrom);
            transaction.setTransactionResult("Successful");
            transaction.setAccountId(accountFrom);
            transaction.setTransactionType("Withdraw");
            transactionService.saveTransaction(transaction);
            return new ResponseEntity<>("Withdrawal successful.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing withdrawal.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Account accountTo = accountService.findByAccountNumber(transaction.getAccountTo());
        if (!accountTo.getUser().getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        try {
            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountService.saveAccount(accountTo);
            transaction.setTransactionResult("Successful");
            transaction.setAccountId(accountTo);
            transaction.setTransactionType("Deposit");
            transactionService.saveTransaction(transaction);
            return new ResponseEntity<>("Deposit successful.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing deposit.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(@RequestBody AccountNoRequest account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Account account_ = accountService.findByAccountNumber(account.accountNo());

        if (account_ == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (!account_.getUser().getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Transaction> transactions = transactionService.findByAccount(account_);
            StringBuilder formattedResponse = new StringBuilder();

            formattedResponse.append("Transaction Statement for Account: ").append(account.accountNo()).append("\n");
            formattedResponse.append("---------------------------------------------------------\n");

            for (Transaction tx : transactions) {
                formattedResponse.append("Transaction Type: ").append(tx.getTransactionType()).append("\n");

                if (tx.getBeneficiary() != null && !tx.getBeneficiary().isEmpty()) {
                    formattedResponse.append("Beneficiary: ").append(tx.getBeneficiary())
                            .append(" (")
                    .append(tx.getAccountNo()).append(")").append("\n");
                }

                if (tx.getTransactionType().equals("Deposit")) {
                    // For Deposits, only show relevant fields
                    if (tx.getAccountTo() != null && !tx.getAccountTo().isEmpty()) {
                        formattedResponse.append("To Account: ").append(tx.getAccountTo()).append("\n");
                    }
                } else if (tx.getTransactionType().equals("Withdraw")) {
                    // For Withdrawals, only show relevant fields
                    if (tx.getAccountFrom() != null && !tx.getAccountFrom().isEmpty()) {
                        formattedResponse.append("From Account: ").append(tx.getAccountFrom()).append("\n");
                    }
                } else {
                    // For Payments and Transfers, show 'To Account'
                    if (tx.getAccountTo() != null && !tx.getAccountTo().isEmpty()) {
                        formattedResponse.append("To Account: ").append(tx.getAccountTo()).append("\n");
                    }
                }

                if (tx.getAmount() != null) {
                    formattedResponse.append("Amount: ").append(tx.getAmount()).append("\n");
                }
                if (tx.getReference() != null && !tx.getReference().isEmpty()) {
                    formattedResponse.append("Reference: ").append(tx.getReference()).append("\n");
                }
                if (tx.getTransactionResult() != null && !tx.getTransactionResult().isEmpty()) {
                    formattedResponse.append("Payment Result: ").append(tx.getTransactionResult()).append("\n");
                }
                if (tx.getTransactionDate() != null) {
                    formattedResponse.append("Date: ").append(tx.getTransactionDate().toString()).append("\n");
                }
                formattedResponse.append("---------------------------------------------------------\n");
            }

            return new ResponseEntity<>(formattedResponse.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching transactions.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
