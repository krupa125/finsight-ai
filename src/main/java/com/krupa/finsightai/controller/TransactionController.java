package com.krupa.finsightai.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/filter")
    public Page<Transaction> getTransactionsByDate(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("timestamp").descending()
        );

        return transactionService.getTransactionsByDate(
                LocalDate.parse(fromDate),
                LocalDate.parse(toDate),
                pageable
        );
    }
}