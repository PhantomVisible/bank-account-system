package com.banking.system.adapter.out.persistence.postgres.repository;

import com.banking.system.adapter.out.persistence.postgres.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository - handles actual database operations
 * This is a TECHNICAL implementation that Spring provides automatically
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    // Spring Data JPA automatically implements these methods!

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}