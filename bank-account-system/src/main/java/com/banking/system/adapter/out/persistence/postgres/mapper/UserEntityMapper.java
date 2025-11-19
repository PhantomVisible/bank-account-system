package com.banking.system.adapter.out.persistence.postgres.mapper;

import com.banking.system.adapter.out.persistence.postgres.entity.UserEntity;
import com.banking.system.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * MAPPER - Converts between Domain Model and JPA Entity
 * This keeps the conversion logic in one place
 */
@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setFullName(user.getFullName());
        entity.setBlocked(user.isBlocked());
        entity.setRole(user.getRole());

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setFullName(entity.getFullName());
        user.setBlocked(entity.isBlocked());
        user.setRole(entity.getRole());

        return user;
    }
}