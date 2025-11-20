package com.banking.system.adapter.out.persistence.mongodb.repository;

import com.banking.system.adapter.out.persistence.mongodb.document.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {

    // Find all transactions for a specific account (as sender or receiver)
    List<TransactionDocument> findByFromAccountOrToAccount(String fromAccount, String toAccount);

    // Find transactions by type (DEPOSIT, WITHDRAW, TRANSFER)
    List<TransactionDocument> findByType(String type);

    // Find transactions by status (SUCCESS, FAILED)
    List<TransactionDocument> findByStatus(String status);

    // Find transactions for account statement
    List<TransactionDocument> findByFromAccount(String fromAccount);
    List<TransactionDocument> findByToAccount(String toAccount);
}