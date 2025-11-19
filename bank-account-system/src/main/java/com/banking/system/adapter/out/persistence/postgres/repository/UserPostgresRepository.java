package com.banking.system.adapter.out.persistence.postgres.repository;

import com.banking.system.adapter.out.persistence.postgres.entity.UserEntity;
import com.banking.system.adapter.out.persistence.postgres.mapper.UserEntityMapper;
import com.banking.system.application.port.out.persistence.UserRepository;
import com.banking.system.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ADAPTER - Implements our UserRepository PORT using PostgreSQL
 * This converts between Domain Objects and JPA Entities
 */
@Component  // Spring bean that can be injected
public class UserPostgresRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    // Constructor injection
    public UserPostgresRepository(UserJpaRepository userJpaRepository,
                                  UserEntityMapper userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User save(User user) {
        // Convert Domain User to JPA Entity
        UserEntity userEntity = userEntityMapper.toEntity(user);

        // Save to database using Spring Data JPA
        UserEntity savedEntity = userJpaRepository.save(userEntity);

        // Convert back to Domain User and return
        return userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        // Find entity by ID
        Optional<UserEntity> userEntity = userJpaRepository.findById(id);

        // Convert to Domain User if found
        return userEntity.map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserEntity> userEntity = userJpaRepository.findByUsername(username);
        return userEntity.map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = userJpaRepository.findByEmail(email);
        return userEntity.map(userEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        // Get all entities from database
        List<UserEntity> userEntities = userJpaRepository.findAll();

        // Convert each entity to domain object
        return userEntities.stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }
}