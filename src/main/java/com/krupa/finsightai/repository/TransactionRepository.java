package com.krupa.finsightai.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.krupa.finsightai.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByTimestampBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<Transaction> findByFromUserIdOrToUserIdAndTimestampBetween(
            Long fromUserId,
            Long toUserId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}