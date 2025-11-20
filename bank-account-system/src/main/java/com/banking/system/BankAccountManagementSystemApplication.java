package com.banking.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main Spring Boot Application Class
 * This is the entry point of our banking system
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.banking.system.adapter.out.persistence.mongodb.repository")
public class BankAccountManagementSystemApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Starting Bank Account Management System...");
        SpringApplication.run(BankAccountManagementSystemApplication.class, args);
        System.out.println("✅ Banking System Started Successfully!");
        System.out.println("🌐 Application running at: http://localhost:8080");
        System.out.println("📚 API Documentation:");
        System.out.println("   Users: http://localhost:8080/api/users");
        System.out.println("   Accounts: http://localhost:8080/api/accounts");
        System.out.println("   Transactions: http://localhost:8080/api/transactions");
        System.out.println("   PDF: http://localhost:8080/api/pdf");
    }
}