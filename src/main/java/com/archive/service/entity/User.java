package com.archive.service.entity;

import com.archive.service.model.UserRoles;
import com.archive.service.util.UserRoleConvertor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Column(name = "roles", nullable = false)
    @Convert(converter = UserRoleConvertor.class)
    private Set<UserRoles> roles;
}
