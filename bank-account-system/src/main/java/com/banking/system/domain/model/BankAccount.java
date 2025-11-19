package com.banking.system.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccount {
    private Long id;
    private String accountNumber;  // Format: "ACC123456789"
    private BigDecimal balance;    // Use BigDecimal for money to avoid floating-point errors
    private String currency;       // "USD", "EUR" etc.
    private User user;             // Each account belongs to one user

    // Default constructor (required by JPA)
    public BankAccount() {
        this.balance = BigDecimal.ZERO;  // Start with zero balance
        this.currency = "USD";           // Default currency
    }

    // Constructor for creating new accounts
    public BankAccount(String accountNumber, User user) {
        this();
        this.accountNumber = accountNumber;
        this.user = user;
    }

    // Constructor with custom currency
    public BankAccount(String accountNumber, User user, String currency) {
        this(accountNumber, user);
        this.currency = currency;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Business Methods - Core Banking Operations

    /**
     * Deposit money into the account
     */
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    /**
     * Withdraw money from the account
     */
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    /**
     * Check if account has sufficient balance
     */
    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }

    /**
     * Get formatted balance string
     */
    public String getFormattedBalance() {
        return String.format("%s %.2f", currency, balance);
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber);
    }

    // toString for debugging
    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + getFormattedBalance() +
                ", currency='" + currency + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                '}';
    }
}