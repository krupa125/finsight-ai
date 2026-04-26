package com.krupa.finsightai.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private Double amount;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // CREDIT / DEBIT

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // SUCCESS / FAILED

    private String description;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.SUCCESS; // default
    }

    public Long getId() { return id; }

    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }

    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}