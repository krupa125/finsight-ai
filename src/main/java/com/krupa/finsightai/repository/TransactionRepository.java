package com.krupa.finsightai.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 🔹 For full history
    Page<Transaction> findByFromUserIdOrToUserId(
        Long fromUserId,
        Long toUserId,
        Pageable pageable
    );

    // 🔥 NEW — for DEBIT (sender side)
    Page<Transaction> findByFromUserIdAndType(
        Long fromUserId,
        TransactionType type,
        Pageable pageable
    );

    // 🔥 NEW — for CREDIT (receiver side)
    Page<Transaction> findByToUserIdAndType(
        Long toUserId,
        TransactionType type,
        Pageable pageable
    );

    @Query("SELECT t FROM Transaction t WHERE (t.fromUserId = :userId OR t.toUserId = :userId) AND t.type = :type")
    Page<Transaction> findTransactionsByUserAndType(
    @Param("userId") Long userId,
    @Param("type") TransactionType type,
    Pageable pageable
    );
}