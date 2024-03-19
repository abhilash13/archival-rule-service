package com.archive.ruleservice.dao;

import com.archive.ruleservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {
    public User findByUsername(String username);
}