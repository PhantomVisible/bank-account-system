package com.banking.system.adapter.in.web;

import com.banking.system.application.port.in.AccountUseCase;
import com.banking.system.application.port.out.persistence.TransactionRepository;
import com.banking.system.domain.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AccountUseCase accountUseCase;
    private final TransactionRepository transactionRepository;

    public TransactionController(AccountUseCase accountUseCase,
                                 TransactionRepository transactionRepository) {
        this.accountUseCase = accountUseCase;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountUseCase.TransferResult> transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam BigDecimal amount) {
        try {
            AccountUseCase.TransferResult result =
                    accountUseCase.transfer(fromAccount, toAccount, amount);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> getAccountTransactions(@PathVariable String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    @GetMapping("/type/{type}")
    public List<Transaction> getTransactionsByType(@PathVariable String type) {
        return transactionRepository.findByType(type);
    }

    @GetMapping("/status/{status}")
    public List<Transaction> getTransactionsByStatus(@PathVariable String status) {
        return transactionRepository.findByStatus(status);
    }
}