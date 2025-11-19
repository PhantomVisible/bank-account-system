package com.banking.system.application.port.out.persistence;

import com.banking.system.domain.model.BankAccount;
import java.util.List;
import java.util.Optional;

/**
 * OUTPUT PORT for Bank Account persistence operations
 * Defines what our application NEEDS for bank account data storage
 */
public interface BankAccountRepository {

    /**
     * Save a bank account to the database
     * @param account the account to save
     * @return the saved account (with generated ID)
     */
    BankAccount save(BankAccount account);

    /**
     * Find a bank account by its ID
     * @param id the account ID
     * @return Optional containing account if found, empty if not found
     */
    Optional<BankAccount> findById(Long id);

    /**
     * Find a bank account by account number
     * @param accountNumber the unique account number
     * @return Optional containing account if found, empty if not found
     */
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts for a specific user
     * @param userId the user ID
     * @return list of user's accounts
     */
    List<BankAccount> findByUserId(Long userId);

    /**
     * Check if an account number already exists
     * @param accountNumber the account number to check
     * @return true if account number exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Find all accounts (for admin purposes)
     * @return list of all accounts
     */
    List<BankAccount> findAll();

    /**
     * Delete an account by ID
     * @param id the account ID to delete
     */
    void deleteById(Long id);

    /**
     * Update account balance
     * @param accountId the account ID
     * @param newBalance the new balance to set
     */
    void updateBalance(Long accountId, java.math.BigDecimal newBalance);
}