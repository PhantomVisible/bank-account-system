package com.banking.system.application.port.in;

import com.banking.system.domain.model.BankAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * INPUT PORT for Bank Account operations
 * Defines what business operations can be performed with bank accounts
 */
public interface AccountUseCase {

    /**
     * Create a new bank account for a user
     * @param userId the user ID who owns the account
     * @param currency the account currency (e.g., "USD", "EUR")
     * @return the created bank account
     */
    BankAccount createAccount(Long userId, String currency);

    /**
     * Get account details by account ID
     * @param accountId the account ID
     * @return the account if found
     */
    Optional<BankAccount> getAccountById(Long accountId);

    /**
     * Get account details by account number
     * @param accountNumber the account number
     * @return the account if found
     */
    Optional<BankAccount> getAccountByNumber(String accountNumber);

    /**
     * Get all accounts for a specific user
     * @param userId the user ID
     * @return list of user's accounts
     */
    List<BankAccount> getUserAccounts(Long userId);

    /**
     * Deposit money into an account
     * @param accountNumber the target account number
     * @param amount the amount to deposit
     * @return the updated account
     */
    BankAccount deposit(String accountNumber, BigDecimal amount);

    /**
     * Withdraw money from an account
     * @param accountNumber the source account number
     * @param amount the amount to withdraw
     * @return the updated account
     */
    BankAccount withdraw(String accountNumber, BigDecimal amount);

    /**
     * Transfer money between two accounts (same currency)
     * @param fromAccountNumber the source account number
     * @param toAccountNumber the target account number
     * @param amount the amount to transfer
     * @return the transfer transaction details
     */
    TransferResult transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);

    /**
     * Get current account balance
     * @param accountNumber the account number
     * @return the current balance
     */
    BigDecimal getBalance(String accountNumber);

    /**
     * Check if account has sufficient balance for a transaction
     * @param accountNumber the account number
     * @param amount the amount to check
     * @return true if sufficient balance exists
     */
    boolean hasSufficientBalance(String accountNumber, BigDecimal amount);

    // Simple record to return transfer results
    record TransferResult(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount, String currency) {}
}