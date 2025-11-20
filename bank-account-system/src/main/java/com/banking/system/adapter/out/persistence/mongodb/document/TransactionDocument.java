package com.banking.system.adapter.out.persistence.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")  // MongoDB collection name
public class TransactionDocument {

    @Id
    private String id;  // MongoDB uses String IDs

    @Field("transaction_id")
    private String transactionId;  // Our business transaction ID

    @Field("from_account")
    private String fromAccount;

    @Field("to_account")
    private String toAccount;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("type")
    private String type;  // "DEPOSIT", "WITHDRAW", "TRANSFER"

    @Field("status")
    private String status;  // "SUCCESS", "FAILED"

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("description")
    private String description;

    // Default constructor
    public TransactionDocument() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for transactions
    public TransactionDocument(String transactionId, String fromAccount, String toAccount,
                               BigDecimal amount, String currency, String type, String status,
                               String description) {
        this();
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}