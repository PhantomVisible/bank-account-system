package com.banking.system.adapter.out.persistence.mongodb.repository;

import com.banking.system.adapter.out.persistence.mongodb.document.TransactionDocument;
import com.banking.system.adapter.out.persistence.mongodb.mapper.TransactionDocumentMapper;
import com.banking.system.application.port.out.persistence.TransactionRepository;
import com.banking.system.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMongoAdapter implements TransactionRepository {

    private final TransactionMongoRepository transactionMongoRepository;
    private final TransactionDocumentMapper transactionDocumentMapper;

    public TransactionMongoAdapter(TransactionMongoRepository transactionMongoRepository,
                                   TransactionDocumentMapper transactionDocumentMapper) {
        this.transactionMongoRepository = transactionMongoRepository;
        this.transactionDocumentMapper = transactionDocumentMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        // Convert Domain Transaction to MongoDB Document
        TransactionDocument document = transactionDocumentMapper.toDocument(transaction);

        // Save to MongoDB
        TransactionDocument savedDocument = transactionMongoRepository.save(document);

        // Convert back to Domain and return
        return transactionDocumentMapper.toDomain(savedDocument);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        // Find all transactions where account was sender OR receiver
        List<TransactionDocument> documents =
                transactionMongoRepository.findByFromAccountOrToAccount(accountNumber, accountNumber);

        // Convert all documents to domain objects
        return documents.stream()
                .map(transactionDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByType(String type) {
        List<TransactionDocument> documents = transactionMongoRepository.findByType(type);
        return documents.stream()
                .map(transactionDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByStatus(String status) {
        List<TransactionDocument> documents = transactionMongoRepository.findByStatus(status);
        return documents.stream()
                .map(transactionDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }
}