package com.ishaank.Projects.GuardianBank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String beneficiary;
    private String accountNo;
    private String accountFrom;
    private String accountTo;
    private Double amount;
    private String reference;
    private String transactionResult;
    private String transactionType;
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account accountId;
}
