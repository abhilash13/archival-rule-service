package com.archive.ruleservice.service.impl;

import com.archive.ruleservice.dao.UsersRepository;
import com.archive.ruleservice.entity.User;
import com.archive.ruleservice.model.CustomUserDetails;
import com.archive.ruleservice.service.IUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements IUserDetailsService {
    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = usersRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
