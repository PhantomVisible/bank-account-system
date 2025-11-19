package com.banking.system.domain.model;

import java.util.Objects;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private boolean blocked = false;  // Default to not blocked
    private String role = "CUSTOMER"; // Default role

    public User() {
    }

    // Constructor for creating new users
    public User(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Business method: Check if user can perform actions
    public boolean canPerformOperations() {
        return !blocked;
    }

    // Business method: Block the user
    public void blockUser() {
        this.blocked = true;
    }

    // Business method: Unblock the user
    public void unblockUser() {
        this.blocked = false;
    }

    // equals and hashCode (important for JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    // toString for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", blocked=" + blocked +
                ", role='" + role + '\'' +
                '}';
    }
}