package com.krupa.finsightai.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krupa.finsightai.exception.ResourceNotFoundException;
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

    public UserService(UserRepository userRepository,
                       TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // ✅ Add User
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get User by ID
   public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // ✅ Update User
    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            return userRepository.save(existing);
        }
        return null;
    }

    // ✅ Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔥 TRANSFER MONEY
    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, Double amount) {

        User fromUser = userRepository.findById(fromUserId)
        .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        User toUser = userRepository.findById(toUserId)
        .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }

        if (fromUser.getBalance() < amount) {
        throw new IllegalArgumentException("Insufficient balance");
        }
        // Update balances
        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // DEBIT
        Transaction debitTxn = new Transaction();
        debitTxn.setFromUserId(fromUserId);
        debitTxn.setToUserId(toUserId);
        debitTxn.setAmount(amount);
        debitTxn.setType(TransactionType.DEBIT);
        debitTxn.setStatus(TransactionStatus.SUCCESS);
        debitTxn.setDescription("Sent to user " + toUserId);

        // CREDIT
        Transaction creditTxn = new Transaction();
        creditTxn.setFromUserId(fromUserId);
        creditTxn.setToUserId(toUserId);
        creditTxn.setAmount(amount);
        creditTxn.setType(TransactionType.CREDIT);
        creditTxn.setStatus(TransactionStatus.SUCCESS);
        creditTxn.setDescription("Received from user " + fromUserId);

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);
    }

    // 🔥 COMBINED TRANSACTION HISTORY (FIXED)
    public Page<Transaction> getUserTransactions(Long userId, int page, int size) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by("timestamp").descending()
    );

    Page<Transaction> transactions =
        transactionRepository.findByFromUserIdOrToUserId(userId, userId, pageable);

    transactions.forEach(txn -> {
        if (txn.getFromUserId().equals(userId)) {
            txn.setDescription("Sent to user " + txn.getToUserId());
        } else {
            txn.setDescription("Received from user " + txn.getFromUserId());
        }
    });

    return transactions;
    }

    // 🔥 FILTER BY TYPE (FIXED LOGIC)
    public Page<Transaction> getTransactionsByType(
            Long userId,
            TransactionType type,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("timestamp").descending()
        );

        return transactionRepository
        .findTransactionsByUserAndType(userId, type, pageable);
    }
}