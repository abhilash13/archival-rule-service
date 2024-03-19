package com.archive.ruleservice.controller;

import com.archive.ruleservice.dao.UsersRepository;
import com.archive.ruleservice.model.LoginRequest;
import com.archive.ruleservice.model.UserRoles;
import com.archive.ruleservice.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var user = usersRepository.findByUsername(loginRequest.getUsername());
            if(user.getPassword().equals(passwordEncoder.encode(loginRequest.getPassword()))){
                return new ResponseEntity<>("Unauthorised", HttpStatus.UNAUTHORIZED);
            }
            List<UserRoles> roles = List.copyOf(user.getRoles());
            String token = tokenProvider.generateToken((UserDetails) authentication.getPrincipal(), roles);

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.error("Exception occurred while creating jwt for user {}", e.getMessage());
            return new ResponseEntity<>("Unauthorised", HttpStatus.UNAUTHORIZED);
        }
    }
}
