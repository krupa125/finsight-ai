package com.krupa.finsightai.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.krupa.finsightai.model.Role;
import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.TransactionRepository;
import com.krupa.finsightai.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnTransactionsForAdmin() {

        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin",
                        null
                ));

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(admin));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Transaction> page =
                new PageImpl<>(java.util.List.of(new Transaction()));

        when(transactionRepository.findByTimestampBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(pageable)))
                .thenReturn(page);

        Page<Transaction> result =
                transactionService.getTransactionsByDate(
                        LocalDate.now().minusDays(5),
                        LocalDate.now(),
                        pageable);

        assertEquals(1, result.getContent().size());

        verify(transactionRepository)
                .findByTimestampBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        eq(pageable));
    }

    @Test
    void shouldReturnTransactionsForNormalUser() {

        User user = new User();
        user.setId(2L);
        user.setUsername("user");
        user.setRole(Role.USER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null
                ));

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Transaction> page =
                new PageImpl<>(java.util.List.of(new Transaction()));

        when(transactionRepository
                .findByFromUserIdOrToUserIdAndTimestampBetween(
                        eq(2L),
                        eq(2L),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        eq(pageable)))
                .thenReturn(page);

        Page<Transaction> result =
                transactionService.getTransactionsByDate(
                        LocalDate.now().minusDays(5),
                        LocalDate.now(),
                        pageable);

        assertEquals(1, result.getContent().size());

        verify(transactionRepository)
                .findByFromUserIdOrToUserIdAndTimestampBetween(
                        eq(2L),
                        eq(2L),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        eq(pageable));
    }

    @Test
    void shouldThrowWhenFromDateAfterToDate() {

        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> transactionService.getTransactionsByDate(
                                LocalDate.now(),
                                LocalDate.now().minusDays(1),
                                pageable));

        assertEquals(
                "fromDate cannot be after toDate",
                exception.getMessage());
    }
}