package com.ishaank.Projects.GuardianBank.service;

import com.ishaank.Projects.GuardianBank.entity.BlacklistedToken;
import com.ishaank.Projects.GuardianBank.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistedTokenService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(BlacklistedToken blacklistedToken) {
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean tokenExists(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
