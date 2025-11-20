package com.banking.system.config;

import com.banking.system.adapter.out.persistence.postgres.entity.UserEntity;
import com.banking.system.adapter.out.persistence.postgres.entity.BankAccountEntity;
import com.banking.system.adapter.out.persistence.postgres.repository.UserJpaRepository;
import com.banking.system.adapter.out.persistence.postgres.repository.BankAccountJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserJpaRepository userJpaRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;

    public DataLoader(UserJpaRepository userJpaRepository,
                      BankAccountJpaRepository bankAccountJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only insert data if no users exist (prevents duplicates)
        if (userJpaRepository.count() == 0) {
            System.out.println("📊 Loading test data into database...");

            // Create and save users first
            UserEntity john = new UserEntity("john_doe", "john@email.com", "password123", "John Doe");
            john.setRole("CUSTOMER");
            john = userJpaRepository.save(john);

            UserEntity jane = new UserEntity("jane_smith", "jane@email.com", "password123", "Jane Smith");
            jane.setRole("CUSTOMER");
            jane = userJpaRepository.save(jane);

            UserEntity manager = new UserEntity("bank_manager", "manager@bank.com", "admin123", "Bank Manager");
            manager.setRole("MANAGER");
            manager = userJpaRepository.save(manager);

            // Create and save bank accounts
            BankAccountEntity johnAccount = new BankAccountEntity("ACC123456", john, "USD");
            johnAccount.setBalance(new BigDecimal("1000.00"));
            bankAccountJpaRepository.save(johnAccount);

            BankAccountEntity janeAccount = new BankAccountEntity("ACC789012", jane, "USD");
            janeAccount.setBalance(new BigDecimal("500.00"));
            bankAccountJpaRepository.save(janeAccount);

            BankAccountEntity managerAccount = new BankAccountEntity("ACC345678", manager, "EUR");
            managerAccount.setBalance(new BigDecimal("2500.00"));
            bankAccountJpaRepository.save(managerAccount);

            System.out.println("✅ Test data loaded successfully!");
            System.out.println("👤 Users created: 3");
            System.out.println("🏦 Accounts created: 3");
        } else {
            System.out.println("ℹ️ Database already has data. Skipping data loading.");
            System.out.println("👤 Existing users: " + userJpaRepository.count());
            System.out.println("🏦 Existing accounts: " + bankAccountJpaRepository.count());
        }
    }
}