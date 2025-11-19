package com.banking.system.application.port.in;

import com.banking.system.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * This is an INPUT PORT - it defines what our application CAN DO (use cases)
 * These are the business operations that external actors (like controllers) can call
 */
public interface UserUseCase {

    /**
     * Register a new user with the system
     * @param user the user to register (without ID)
     * @return the registered user (with generated ID and bank account)
     */
    User registerUser(User user);

    /**
     * Get user details by ID
     * @param userId the user ID
     * @return the user if found
     */
    Optional<User> getUserById(Long userId);

    /**
     * Get user details by username
     * @param username the username
     * @return the user if found
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Get all users (for admin/manager purposes)
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Block a user - prevents them from performing transactions
     * @param userId the user ID to block
     * @return the blocked user
     */
    User blockUser(Long userId);

    /**
     * Unblock a user - allows them to perform transactions again
     * @param userId the user ID to unblock
     * @return the unblocked user
     */
    User unblockUser(Long userId);

    /**
     * Check if a user can perform operations (not blocked)
     * @param userId the user ID to check
     * @return true if user can perform operations, false if blocked
     */
    boolean canUserPerformOperations(Long userId);

    /**
     * Validate user credentials for login
     * @param username the username
     * @param password the password
     * @return the user if credentials are valid
     */
    Optional<User> validateCredentials(String username, String password);
}