package com.krupa.finsightai.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.krupa.finsightai.model.Role;
import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.TransactionRepository;
import com.krupa.finsightai.repository.UserRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Page<Transaction> getTransactionsByDate(
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate cannot be after toDate");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.atTime(23, 59, 59);

        if (currentUser.getRole() == Role.ADMIN) {
            return transactionRepository.findByTimestampBetween(
                    start,
                    end,
                    pageable
            );
        }

        return transactionRepository
        .findUserTransactions(
                currentUser.getId(),
                start,
                end,
                pageable
        );
    }
}