package com.banking.system.adapter.in.web;

import com.banking.system.adapter.out.persistence.postgres.repository.BankAccountJpaRepository;
import com.banking.system.adapter.out.persistence.postgres.repository.UserJpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public DebugController(BankAccountJpaRepository bankAccountJpaRepository,
                           UserJpaRepository userJpaRepository) {
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @GetMapping("/database")
    public String debugDatabase() {
        StringBuilder result = new StringBuilder();

        result.append("=== DATABASE DEBUG INFO ===\n\n");

        // User info
        result.append("USERS:\n");
        userJpaRepository.findAll().forEach(user -> {
            result.append("  ID: ").append(user.getId())
                    .append(", Username: ").append(user.getUsername())
                    .append(", Email: ").append(user.getEmail())
                    .append("\n");
        });

        result.append("\nBANK ACCOUNTS:\n");
        bankAccountJpaRepository.findAll().forEach(account -> {
            result.append("  Account: ").append(account.getAccountNumber())
                    .append(", Balance: ").append(account.getBalance())
                    .append(", Currency: ").append(account.getCurrency())
                    .append(", User ID: ").append(account.getUser() != null ? account.getUser().getId() : "NULL")
                    .append("\n");
        });

        // Test specific queries
        result.append("\nSPECIFIC QUERIES:\n");
        result.append("Account ACC123456 exists: ").append(bankAccountJpaRepository.findByAccountNumber("ACC123456").isPresent()).append("\n");
        result.append("Total accounts: ").append(bankAccountJpaRepository.count()).append("\n");

        return result.toString();
    }
}