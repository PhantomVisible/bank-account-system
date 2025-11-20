package com.banking.system.adapter.aop;

import com.banking.system.application.port.out.persistence.TransactionRepository;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.Transaction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect
@Component
public class TransactionLoggingAspect {

    private final TransactionRepository transactionRepository;

    public TransactionLoggingAspect(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @AfterReturning(pointcut = "@annotation(logTransaction)", returning = "result")
    public void logTransaction(JoinPoint joinPoint, LogTransaction logTransaction, Object result) {
        try {
            System.out.println("🔍 AOP: Logging " + logTransaction.type() + " transaction to MongoDB...");

            Transaction transaction = createTransaction(joinPoint, logTransaction, result);
            transactionRepository.save(transaction);

            System.out.println("✅ Transaction logged to MongoDB: " + transaction.getTransactionId());

        } catch (Exception e) {
            System.err.println("❌ Failed to log transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Transaction createTransaction(JoinPoint joinPoint, LogTransaction logTransaction, Object result) {
        Object[] args = joinPoint.getArgs();
        String type = logTransaction.type();

        // Handle DEPOSIT
        if ("DEPOSIT".equals(type) && args.length >= 2) {
            String accountNumber = (String) args[0];
            BigDecimal amount = (BigDecimal) args[1];
            return new Transaction(null, accountNumber, amount, "USD", type, "SUCCESS",
                    "Deposit to account: " + accountNumber);
        }

        // Handle WITHDRAW
        if ("WITHDRAW".equals(type) && args.length >= 2) {
            String accountNumber = (String) args[0];
            BigDecimal amount = (BigDecimal) args[1];
            return new Transaction(accountNumber, null, amount, "USD", type, "SUCCESS",
                    "Withdrawal from account: " + accountNumber);
        }

        // Handle TRANSFER
        if ("TRANSFER".equals(type) && args.length >= 3) {
            String fromAccount = (String) args[0];
            String toAccount = (String) args[1];
            BigDecimal amount = (BigDecimal) args[2];
            return new Transaction(fromAccount, toAccount, amount, "USD", type, "SUCCESS",
                    "Transfer from " + fromAccount + " to " + toAccount);
        }

        // Fallback for other methods
        return new Transaction("SYSTEM", "SYSTEM", BigDecimal.ZERO, "USD", type, "SUCCESS",
                logTransaction.description());
    }
}