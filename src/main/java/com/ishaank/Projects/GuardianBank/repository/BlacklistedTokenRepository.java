package com.ishaank.Projects.GuardianBank.repository;

import com.ishaank.Projects.GuardianBank.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);
}
