package com.archive.ruleservice.util;

import com.archive.ruleservice.model.UserRoles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class UserRoleConvertor implements AttributeConverter<Set<UserRoles>, String> {

    @Override
    public String convertToDatabaseColumn(Set<UserRoles> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<UserRoles> convertToEntityAttribute(String roles) {
        return Arrays.stream(roles.split(","))
                .map(UserRoles::valueOf)
                .collect(Collectors.toSet());
    }
}
