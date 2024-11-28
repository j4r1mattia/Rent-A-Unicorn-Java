package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.Role;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.model.User;
import com.rentunicorn.unirent.repository.UserRepository;
import com.rentunicorn.unirent.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = TestUtils.createUser(1L, "user1", "user1@example.com", "password", Collections.singleton(Role.USER));
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        String username = "user1";
        String email = "user1@example.com";
        String password = "password";

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User registeredUser = userService.registerUser(username, email, password);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        String username = "user1";
        String email = "user1@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(username, email, password)
        );
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        String email = "user1@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        // Act
        Optional<User> authenticatedUser = userService.authenticateUser(email, password);

        // Assert
        assertTrue(authenticatedUser.isPresent());
        assertEquals(email, authenticatedUser.get().getEmail());
    }

    @Test
    void testAuthenticateUser_Failure_InvalidPassword() {
        // Arrange
        String email = "user1@example.com";
        String password = "wrongpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticateUser(email, password);
        });

        // Assert
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Failure_UserNotFound() {
        // Arrange
        String email = "user1@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticateUser(email, password);
        });

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> user = userService.getUserById(userId);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(userId, user.get().getId());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> user = userService.getUserById(userId);

        // Assert
        assertFalse(user.isPresent());
    }
}

