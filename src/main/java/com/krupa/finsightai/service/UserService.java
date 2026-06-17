package com.krupa.finsightai.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krupa.finsightai.exception.ResourceNotFoundException;
import com.krupa.finsightai.model.Role;
import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.TransactionStatus;
import com.krupa.finsightai.model.TransactionType;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.TransactionRepository;
import com.krupa.finsightai.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       TransactionRepository transactionRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User addUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getUsername() != null &&
            !updatedUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null &&
            !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getName() != null &&
            !updatedUser.getName().isEmpty()) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getPassword() != null &&
            !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Transactional
    public String transferMoney(Long senderId, Long receiverId, Double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot transfer money to yourself");
        }

        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);

        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setFromUserId(sender.getId());
        debitTransaction.setToUserId(receiver.getId());
        debitTransaction.setAmount(amount);
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setStatus(TransactionStatus.SUCCESS);
        debitTransaction.setDescription("Money sent to " + receiver.getUsername());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setFromUserId(sender.getId());
        creditTransaction.setToUserId(receiver.getId());
        creditTransaction.setAmount(amount);
        creditTransaction.setType(TransactionType.CREDIT);
        creditTransaction.setStatus(TransactionStatus.SUCCESS);
        creditTransaction.setDescription("Money received from " + sender.getUsername());

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return "Transfer successful";
    }

    public String transferMoneyByUsername(String senderUsername,
                                          Long receiverId,
                                          Double amount) {

        User sender = getUserByUsername(senderUsername);
        return transferMoney(sender.getId(), receiverId, amount);
    }

    public Page<Transaction> getUserTransactions(Long userId, int page, int size) {
        return transactionRepository.findByFromUserIdOrToUserIdAndTimestampBetween(
                userId,
                userId,
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now(),
                PageRequest.of(page, size)
        );
    }

    public Page<Transaction> getTransactionsByType(Long userId,
                                                   TransactionType type,
                                                   int page,
                                                   int size) {

        Page<Transaction> transactions =
                transactionRepository.findByFromUserIdOrToUserIdAndTimestampBetween(
                        userId,
                        userId,
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.now(),
                        PageRequest.of(page, size)
                );

        return new PageImpl<>(
                transactions.getContent()
                        .stream()
                        .filter(transaction -> transaction.getType() == type)
                        .toList(),
                transactions.getPageable(),
                transactions.getContent().size()
        );
    }
}