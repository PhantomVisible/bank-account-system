package com.banking.system.application.service;

import com.banking.system.adapter.aop.LogTransaction;
import com.banking.system.application.port.in.AccountUseCase;
import com.banking.system.application.port.out.persistence.BankAccountRepository;
import com.banking.system.application.port.out.persistence.UserRepository;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * APPLICATION SERVICE - Implements banking use cases
 * Handles bank account operations and transactions
 */
@Service
public class AccountApplicationService implements AccountUseCase {

    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;

    // Constructor injection - Spring provides both repositories
    public AccountApplicationService(BankAccountRepository accountRepository,
                                     UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public BankAccount createAccount(Long userId, String currency) {
        // 1. Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Check if user already has an account (simple - one account per user for now)
        List<BankAccount> existingAccounts = accountRepository.findByUserId(userId);
        if (!existingAccounts.isEmpty()) {
            throw new IllegalArgumentException("User already has an account");
        }

        // 3. Generate unique account number
        String accountNumber = generateAccountNumber();

        // 4. Create new bank account
        BankAccount newAccount = new BankAccount(accountNumber, user, currency);

        // 5. Save to database
        BankAccount savedAccount = accountRepository.save(newAccount);

        return savedAccount;
    }

    @Override
    public Optional<BankAccount> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public Optional<BankAccount> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<BankAccount> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @LogTransaction(type = "DEPOSIT", description = "Money deposited to account")
    public BankAccount deposit(String accountNumber, BigDecimal amount) {
        // 1. Validate amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        // 2. Find the account
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // 3. Check if account owner is blocked
        if (account.getUser().isBlocked()) {
            throw new IllegalArgumentException("Account owner is blocked from transactions");
        }

        // 4. Perform deposit (domain logic)
        account.deposit(amount);

        // 5. Save updated account
        BankAccount updatedAccount = accountRepository.save(account);

        return updatedAccount;
    }

    @Override
    @LogTransaction(type = "WITHDRAW", description = "Money withdrawn from account")
    public BankAccount withdraw(String accountNumber, BigDecimal amount) {
        // 1. Validate amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        // 2. Find the account
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // 3. Check if account owner is blocked
        if (account.getUser().isBlocked()) {
            throw new IllegalArgumentException("Account owner is blocked from transactions");
        }

        // 4. Perform withdrawal (domain logic)
        account.withdraw(amount);

        // 5. Save updated account
        BankAccount updatedAccount = accountRepository.save(account);

        return updatedAccount;
    }

    @Override
    @LogTransaction(type = "TRANSFER", description = "Money transferred between accounts")
    public TransferResult transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        // 1. Validate amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        // 2. Find both accounts
        BankAccount fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found: " + fromAccountNumber));

        BankAccount toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found: " + toAccountNumber));

        // 3. Check if source account owner is blocked
        if (fromAccount.getUser().isBlocked()) {
            throw new IllegalArgumentException("Source account owner is blocked from transactions");
        }

        // 4. Check if accounts have same currency
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch - use forex transfer for different currencies");
        }

        // 5. Perform transfer - withdraw from source, deposit to target
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // 6. Save both accounts
        BankAccount updatedFromAccount = accountRepository.save(fromAccount);
        BankAccount updatedToAccount = accountRepository.save(toAccount);

        // 7. Return transfer result
        return new TransferResult(updatedFromAccount, updatedToAccount, amount, fromAccount.getCurrency());
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        return account.getBalance();
    }

    @Override
    public boolean hasSufficientBalance(String accountNumber, BigDecimal amount) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        return account.hasSufficientBalance(amount);
    }

    /**
     * Helper method to generate unique account number
     */
    private String generateAccountNumber() {
        // Simple implementation - in real bank, this would follow specific format
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}