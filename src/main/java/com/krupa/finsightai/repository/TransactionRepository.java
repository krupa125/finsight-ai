package com.krupa.finsightai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krupa.finsightai.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByTimestampBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE (t.fromUserId = :userId OR t.toUserId = :userId)
        AND t.timestamp BETWEEN :startDate AND :endDate
    """)
    Page<Transaction> findUserTransactions(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
           SELECT t.category, SUM(t.amount)
           FROM Transaction t
           WHERE t.fromUserId = :userId
           AND t.timestamp BETWEEN :startDate AND :endDate
           GROUP BY t.category
           """)
    List<Object[]> getCategoryBreakdown(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
   

    Page<Transaction> findByFromUserIdOrToUserIdAndTimestampBetween(Long id, Long id2, LocalDateTime start,
            LocalDateTime end, Pageable pageable);
}