package com.banking.system.adapter.in.web;

import com.banking.system.application.port.in.UserUseCase;
import com.banking.system.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userUseCase.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userId}") // URL: /api/users/123
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return userUseCase.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userUseCase.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userUseCase.getAllUsers();
    }

    @PostMapping("/{userId}/block")
    public ResponseEntity<User> blockUser(@PathVariable Long userId) {
        try {
            User blockedUser = userUseCase.blockUser(userId);
            return ResponseEntity.ok(blockedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/unblock")
    public ResponseEntity<User> unblockUser(@PathVariable Long userId) {
        try {
            User unblockedUser = userUseCase.unblockUser(userId);
            return ResponseEntity.ok(unblockedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<User> validateCredentials(
            @RequestParam String username,
            @RequestParam String password) {
        return userUseCase.validateCredentials(username, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}