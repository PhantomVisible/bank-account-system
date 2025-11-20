package com.banking.system.adapter.in.web;

import com.banking.system.application.port.in.AccountUseCase;
import com.banking.system.domain.model.BankAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountUseCase accountUseCase;

    public AccountController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @GetMapping
    public List<BankAccount> getAllAccounts() {
        return accountUseCase.getAllAccounts();
    }

    @PostMapping("/users/{userId}/create")
    public ResponseEntity<BankAccount> createAccount(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "USD") String currency) {
        try {
            BankAccount account = accountUseCase.createAccount(userId, currency);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable Long accountId) {
        return accountUseCase.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<BankAccount> getAccountByNumber(@PathVariable String accountNumber) {
        return accountUseCase.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<BankAccount> getUserAccounts(@PathVariable Long userId) {
        return accountUseCase.getUserAccounts(userId);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<BankAccount> deposit(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        try {
            BankAccount account = accountUseCase.deposit(accountNumber, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<BankAccount> withdraw(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        try {
            BankAccount account = accountUseCase.withdraw(accountNumber, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        try {
            BigDecimal balance = accountUseCase.getBalance(accountNumber);
            return ResponseEntity.ok(balance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}