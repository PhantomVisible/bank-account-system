package com.banking.system.application.port.out.persistence;

import com.banking.system.domain.model.Transaction;
import java.util.List;

/**
 * OUTPUT PORT for Transaction logging to MongoDB
 */
public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findByAccountNumber(String accountNumber);

    List<Transaction> findByType(String type);

    List<Transaction> findByStatus(String status);
}