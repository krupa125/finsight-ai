package com.krupa.finsightai.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.TransactionType;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔹 CREATE USER
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 🔹 GET ALL USERS
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 🔹 GET USER BY ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 🔹 UPDATE USER
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // 🔹 DELETE USER
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // 🔥 TRANSFER MONEY
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(
        @RequestParam Long fromUserId,
        @RequestParam Long toUserId,
        @RequestParam Double amount) {

    userService.transferMoney(fromUserId, toUserId, amount);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Transfer successful");
    response.put("fromUserId", fromUserId);
    response.put("toUserId", toUserId);
    response.put("amount", amount);

    return ResponseEntity.ok(response);
    }

    // 🔥 GET FULL TRANSACTION HISTORY
    @GetMapping("/transactions/{userId}")
    public Page<Transaction> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return userService.getUserTransactions(userId, page, size);
    }

    // 🔥 FILTER TRANSACTIONS (DEBIT / CREDIT)
    @GetMapping("/transactions/{userId}/filter")
    public Page<Transaction> getTransactionsByType(
            @PathVariable Long userId,
            @RequestParam TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return userService.getTransactionsByType(userId, type, page, size);
    }
}