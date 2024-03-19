package com.archive.ruleservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
