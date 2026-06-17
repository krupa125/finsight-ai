package com.krupa.finsightai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.krupa.finsightai.exception.ResourceNotFoundException;
import com.krupa.finsightai.model.Role;
import com.krupa.finsightai.model.User;
import com.krupa.finsightai.repository.TransactionRepository;
import com.krupa.finsightai.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldSaveUserSuccessfully() {

        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setPassword("password123");

        when(userRepository.existsByUsername("john"))
                .thenReturn(false);

        when(userRepository.existsByEmail("john@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);

        assertEquals("john", savedUser.getUsername());
        assertEquals("john@test.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        verify(userRepository).existsByUsername("john");
        verify(userRepository).existsByEmail("john@test.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameAlreadyExists() {

    User user = new User();
    user.setUsername("john");
    user.setEmail("john@test.com");

    when(userRepository.existsByUsername("john"))
            .thenReturn(true);

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.registerUser(user)
            );

    assertEquals(
            "Username already exists",
            exception.getMessage()
    );

    verify(userRepository).existsByUsername("john");

    verify(userRepository, never())
            .save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

    User user = new User();
    user.setUsername("john");
    user.setEmail("john@test.com");

    when(userRepository.existsByUsername("john"))
            .thenReturn(false);

    when(userRepository.existsByEmail("john@test.com"))
            .thenReturn(true);

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.registerUser(user)
            );

    assertEquals(
            "Email already exists",
            exception.getMessage()
    );

    verify(userRepository).existsByUsername("john");
    verify(userRepository).existsByEmail("john@test.com");

    verify(userRepository, never())
            .save(any(User.class));
    }
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {

    User user = new User();
    user.setId(1L);
    user.setUsername("john");

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(user));

    User result = userService.getUserById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("john", result.getUsername());

    verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.empty());

    ResourceNotFoundException exception =
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.getUserById(1L)
            );

    assertEquals(
            "User not found with id: 1",
            exception.getMessage()
    );

    verify(userRepository).findById(1L);
    }

    @Test
    void transferMoney_ShouldThrowException_WhenAmountIsZero() {

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.transferMoney(
                            1L,
                            2L,
                            0.0
                    )
            );

    assertEquals(
            "Amount must be greater than zero",
            exception.getMessage()
    );
    }
    @Test
void transferMoney_ShouldThrowException_WhenSenderAndReceiverAreSame() {

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.transferMoney(
                            1L,
                            1L,
                            100.0
                    )
            );

    assertEquals(
            "Cannot transfer money to yourself",
            exception.getMessage()
    );
    }

    @Test
void transferMoney_ShouldThrowException_WhenBalanceIsInsufficient() {

    User sender = new User();
    sender.setId(1L);
    sender.setUsername("john");
    sender.setBalance(100.0);

    User receiver = new User();
    receiver.setId(2L);
    receiver.setUsername("alice");
    receiver.setBalance(500.0);

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(sender));

    when(userRepository.findById(2L))
            .thenReturn(java.util.Optional.of(receiver));

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.transferMoney(
                            1L,
                            2L,
                            500.0
                    )
            );

    assertEquals(
            "Insufficient balance",
            exception.getMessage()
    );

    verify(userRepository).findById(1L);
    verify(userRepository).findById(2L);

    verify(userRepository, never())
            .save(any(User.class));

    verify(transactionRepository, never())
            .save(any());
    }

    @Test
void transferMoney_ShouldTransferSuccessfully() {

    User sender = new User();
    sender.setId(1L);
    sender.setUsername("john");
    sender.setBalance(1000.0);

    User receiver = new User();
    receiver.setId(2L);
    receiver.setUsername("alice");
    receiver.setBalance(500.0);

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(sender));

    when(userRepository.findById(2L))
            .thenReturn(java.util.Optional.of(receiver));

    String result = userService.transferMoney(
            1L,
            2L,
            200.0
    );

    assertEquals("Transfer successful", result);

    assertEquals(800.0, sender.getBalance());
    assertEquals(700.0, receiver.getBalance());

    verify(userRepository, times(2))
            .save(any(User.class));

    verify(transactionRepository, times(2))
            .save(any());
    }

    @Test
void getUserByUsername_ShouldReturnUser_WhenUserExists() {

    User user = new User();
    user.setId(1L);
    user.setUsername("john");

    when(userRepository.findByUsername("john"))
            .thenReturn(java.util.Optional.of(user));

    User result = userService.getUserByUsername("john");

    assertNotNull(result);
    assertEquals("john", result.getUsername());

    verify(userRepository).findByUsername("john");
    }

    @Test
void getUserByUsername_ShouldThrowException_WhenUserNotFound() {

    when(userRepository.findByUsername("john"))
            .thenReturn(java.util.Optional.empty());

    ResourceNotFoundException exception =
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.getUserByUsername("john")
            );

    assertEquals(
            "User not found",
            exception.getMessage()
    );

    verify(userRepository).findByUsername("john");
    }

    @Test
void updateUser_ShouldUpdateUserSuccessfully() {

    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setName("Old Name");
    existingUser.setUsername("olduser");
    existingUser.setEmail("old@test.com");
    existingUser.setPassword("oldPassword");

    User updatedUser = new User();
    updatedUser.setName("New Name");
    updatedUser.setUsername("newuser");
    updatedUser.setEmail("new@test.com");
    updatedUser.setPassword("newPassword");

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(existingUser));

    when(passwordEncoder.encode("newPassword"))
            .thenReturn("encodedPassword");

    when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    User result = userService.updateUser(1L, updatedUser);

    assertEquals("New Name", result.getName());
    assertEquals("newuser", result.getUsername());
    assertEquals("new@test.com", result.getEmail());
    assertEquals("encodedPassword", result.getPassword());

    verify(userRepository).findById(1L);
    verify(passwordEncoder).encode("newPassword");
    verify(userRepository).save(any(User.class));
    }

    @Test
void deleteUser_ShouldDeleteUserSuccessfully() {

    User user = new User();
    user.setId(1L);
    user.setUsername("john");

    when(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(user));

    userService.deleteUser(1L);

    verify(userRepository).findById(1L);
    verify(userRepository).delete(user);
    }


}