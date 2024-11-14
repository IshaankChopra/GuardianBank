package com.ishaank.Projects.GuardianBank.entity;

import com.ishaank.Projects.GuardianBank.enumeration.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String bankCode = "001";
    private String accountNumber;
    private Double balance = 0.0d;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User user;

}
