package com.banking.system.application.service;

import com.banking.system.application.port.in.UserUseCase;
import com.banking.system.application.port.out.persistence.UserRepository;
import com.banking.system.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * APPLICATION SERVICE - Implements the use cases (input ports)
 * This class orchestrates the business operations for users
 */
@Service  // This makes it a Spring bean that can be injected
public class UserApplicationService implements UserUseCase {

    private final UserRepository userRepository;

    // Constructor injection - Spring will provide the UserRepository
    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        // 1. Validate that username doesn't already exist
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        // 2. Validate that email doesn't already exist
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // 3. Set default values if needed
        user.setBlocked(false);  // New users are not blocked
        user.setRole("CUSTOMER"); // Default role

        // 4. Save the user to database
        User savedUser = userRepository.save(user);

        // 5. In the future, we would create a bank account here
        // 6. In the future, we would send welcome email here

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        // Simple delegation to repository
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User blockUser(Long userId) {
        // 1. Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Apply business logic - block the user
        user.blockUser();

        // 3. Save the updated user
        User blockedUser = userRepository.save(user);

        return blockedUser;
    }

    @Override
    public User unblockUser(Long userId) {
        // 1. Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Apply business logic - unblock the user
        user.unblockUser();

        // 3. Save the updated user
        User unblockedUser = userRepository.save(user);

        return unblockedUser;
    }

    @Override
    public boolean canUserPerformOperations(Long userId) {
        // 1. Find the user
        Optional<User> user = userRepository.findById(userId);

        // 2. If user doesn't exist or is blocked, return false
        if (user.isEmpty()) {
            return false;
        }

        // 3. Delegate to domain logic
        return user.get().canPerformOperations();
    }

    @Override
    public Optional<User> validateCredentials(String username, String password) {
        // 1. Find user by username
        Optional<User> user = userRepository.findByUsername(username);

        // 2. Check if user exists and password matches
        // NOTE: In real application, we would hash the password!
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }

        // 3. Return empty if credentials are invalid
        return Optional.empty();
    }
}