package com.archive.ruleservice;

import com.archive.ruleservice.dao.UsersRepository;
import com.archive.ruleservice.entity.User;
import com.archive.ruleservice.model.UserRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DummyData implements CommandLineRunner {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        var user = usersRepository.findByUsername("admin");
        if(user != null && user.getUsername().equals("admin")){
            log.debug("User is present, no need to create new");
            return;
        }
        log.debug("No user details are present in the DB.");

        User adminUser = new User();
        adminUser.setUsername("admin");
        String encodedPassword = passwordEncoder.encode("admin123");
        adminUser.setPassword(encodedPassword);
        Set<UserRoles> roles = new HashSet<>();
        roles.add(UserRoles.ADMIN);
        adminUser.setRoles(roles);
        usersRepository.save(adminUser);

        User student = new User();
        student.setUsername("student");
        String studentPassword = passwordEncoder.encode("student123");
        student.setPassword(studentPassword);
        Set<UserRoles> roles1 = new HashSet<>();
        roles1.add(UserRoles.USER);
        student.setRoles(roles1);
        usersRepository.save(student);

        User random = new User();
        random.setUsername("random");
        String randomPassword = passwordEncoder.encode("random123");
        random.setPassword(randomPassword);
        Set<UserRoles> roles2 = new HashSet<>();
        roles2.add(UserRoles.RANDOM);
        random.setRoles(roles2);
        usersRepository.save(random);
    }
}
