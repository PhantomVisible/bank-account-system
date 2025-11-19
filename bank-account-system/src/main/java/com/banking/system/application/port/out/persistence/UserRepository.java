package com.banking.system.application.port.out.persistence;

import com.banking.system.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * This is an OUTPUT PORT - it defines what our application NEEDS from the database
 * Notice: It's an INTERFACE, not a class! We're defining the contract.
 */
public interface UserRepository {

    /**
     * Save a user to the database
     * @param user the user to save
     * @return the saved user (with generated ID)
     */
    User save(User user);

    /**
     * Find a user by their ID
     * @param id the user ID
     * @return Optional containing user if found, empty if not found
     */
    Optional<User> findById(Long id);

    /**
     * Find a user by their username
     * @param username the username to search for
     * @return Optional containing user if found, empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their email
     * @param email the email to search for
     * @return Optional containing user if found, empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username already exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email already exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Get all users (for admin purposes)
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Delete a user by ID
     * @param id the user ID to delete
     */
    void deleteById(Long id);
}