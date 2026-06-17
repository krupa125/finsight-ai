package com.krupa.finsightai.controller;

import java.security.Principal;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ADMIN ONLY
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // ADMIN ONLY
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // USER: own profile / ADMIN: any profile
    @GetMapping("/me")
    public User getMyProfile(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

    // ADMIN ONLY
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // USER updates own profile
    @PutMapping("/me")
    public User updateMyProfile(Principal principal,
                                @RequestBody User user) {

        User currentUser =
                userService.getUserByUsername(principal.getName());

        return userService.updateUser(currentUser.getId(), user);
    }

    // ADMIN ONLY
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // SECURE TRANSFER
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(
            Principal principal,
            @RequestParam Long toUserId,
            @RequestParam Double amount) {

        User sender = userService.getUserByUsername(principal.getName());

        userService.transferMoney(
                sender.getId(),
                toUserId,
                amount
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer successful");
        response.put("sender", sender.getUsername());
        response.put("toUserId", toUserId);
        response.put("amount", amount);

        return ResponseEntity.ok(response);
    }

    // OWN TRANSACTIONS
    @GetMapping("/transactions")
    public Page<Transaction> getUserTransactions(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User user = userService.getUserByUsername(principal.getName());

        return userService.getUserTransactions(
                user.getId(),
                page,
                size
        );
    }

    // OWN FILTERED TRANSACTIONS
    @GetMapping("/transactions/filter")
    public Page<Transaction> getTransactionsByType(
            Principal principal,
            @RequestParam TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User user = userService.getUserByUsername(principal.getName());

        return userService.getTransactionsByType(
                user.getId(),
                type,
                page,
                size
        );
    }
}