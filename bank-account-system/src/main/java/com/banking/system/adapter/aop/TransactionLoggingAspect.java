package com.banking.system.adapter.aop;

import com.banking.system.application.port.out.persistence.TransactionRepository;
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
            // Extract transaction details from method arguments and result
            Transaction transaction = createTransaction(joinPoint, logTransaction, result);

            // Save to MongoDB
            transactionRepository.save(transaction);

            System.out.println("📝 Transaction logged to MongoDB: " + transaction.getTransactionId());

        } catch (Exception e) {
            System.err.println("❌ Failed to log transaction: " + e.getMessage());
        }
    }

    private Transaction createTransaction(JoinPoint joinPoint, LogTransaction logTransaction, Object result) {
        Object[] args = joinPoint.getArgs();

        // For deposit/withdraw methods
        if (args.length >= 2 && args[0] instanceof String && args[1] instanceof BigDecimal) {
            String accountNumber = (String) args[0];
            BigDecimal amount = (BigDecimal) args[1];

            String fromAccount = logTransaction.type().equals("DEPOSIT") ? null : accountNumber;
            String toAccount = logTransaction.type().equals("DEPOSIT") ? accountNumber : null;

            return new Transaction(
                    fromAccount,
                    toAccount,
                    amount,
                    "USD", // You could get this from the account
                    logTransaction.type(),
                    "SUCCESS",
                    logTransaction.description()
            );
        }

        // For transfer method
        if (args.length >= 3 && args[0] instanceof String && args[1] instanceof String && args[2] instanceof BigDecimal) {
            String fromAccount = (String) args[0];
            String toAccount = (String) args[1];
            BigDecimal amount = (BigDecimal) args[2];

            return new Transaction(
                    fromAccount,
                    toAccount,
                    amount,
                    "USD",
                    "TRANSFER",
                    "SUCCESS",
                    "Transfer between accounts"
            );
        }

        // Default fallback
        return new Transaction(
                "UNKNOWN",
                "UNKNOWN",
                BigDecimal.ZERO,
                "USD",
                logTransaction.type(),
                "SUCCESS",
                logTransaction.description()
        );
    }
}