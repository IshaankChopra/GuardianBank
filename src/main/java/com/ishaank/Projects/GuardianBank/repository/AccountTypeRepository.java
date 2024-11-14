package com.ishaank.Projects.GuardianBank.repository;

import com.ishaank.Projects.GuardianBank.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {

    AccountType findByType(String type);
}