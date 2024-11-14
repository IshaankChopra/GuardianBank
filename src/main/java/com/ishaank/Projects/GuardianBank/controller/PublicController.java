package com.ishaank.Projects.GuardianBank.controller;

import com.ishaank.Projects.GuardianBank.dto.LoginRequest;
import com.ishaank.Projects.GuardianBank.entity.BlacklistedToken;
import com.ishaank.Projects.GuardianBank.entity.User;
import com.ishaank.Projects.GuardianBank.impl.UserDetailsServiceImpl;
import com.ishaank.Projects.GuardianBank.repository.BlacklistedTokenRepository;
import com.ishaank.Projects.GuardianBank.service.BlacklistedTokenService;
import com.ishaank.Projects.GuardianBank.service.UserService;
import com.ishaank.Projects.GuardianBank.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class PublicController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BlacklistedTokenService blacklistedTokenService;

    @GetMapping("/ok")
    public String healthCheck() {
        return "OK!";
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.userId(), user.password()));
            //internally calls loadByUsername from UserDetailsServiceImpl and authenticates the user
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.userId());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occured while creating authentication token ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logoutt")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String token = jwtUtil.extractToken(request);
        if (token == null) {
            return new ResponseEntity<>("User is already logged out!", HttpStatus.BAD_REQUEST);
        }
        if (!blacklistedTokenService.tokenExists(token)) {
            blacklistedTokenService.blacklistToken(new BlacklistedToken(token));
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        }
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("User is already logged out!", HttpStatus.BAD_REQUEST);
    }
}
