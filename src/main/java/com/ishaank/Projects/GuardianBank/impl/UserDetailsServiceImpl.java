package com.ishaank.Projects.GuardianBank.impl;

import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserId(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getUserId())
                    .password(user.getPassword())
                    .build();
        }
        throw new UsernameNotFoundException("User not found with userID: " + username);
    }
}
