package com.banking.system.adapter.out.persistence.mongodb.mapper;

import com.banking.system.adapter.out.persistence.mongodb.document.TransactionDocument;
import com.banking.system.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionDocumentMapper {

    public TransactionDocument toDocument(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDocument document = new TransactionDocument();
        document.setTransactionId(transaction.getTransactionId());
        document.setFromAccount(transaction.getFromAccount());
        document.setToAccount(transaction.getToAccount());
        document.setAmount(transaction.getAmount());
        document.setCurrency(transaction.getCurrency());
        document.setType(transaction.getType());
        document.setStatus(transaction.getStatus());
        document.setDescription(transaction.getDescription());
        // timestamp is set automatically in constructor

        return document;
    }

    public Transaction toDomain(TransactionDocument document) {
        if (document == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(document.getTransactionId());
        transaction.setFromAccount(document.getFromAccount());
        transaction.setToAccount(document.getToAccount());
        transaction.setAmount(document.getAmount());
        transaction.setCurrency(document.getCurrency());
        transaction.setType(document.getType());
        transaction.setStatus(document.getStatus());
        transaction.setDescription(document.getDescription());
        transaction.setTimestamp(document.getTimestamp());

        return transaction;
    }
}