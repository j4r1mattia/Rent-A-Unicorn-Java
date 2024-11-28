package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.User;
import com.rentunicorn.unirent.model.Role;
import com.rentunicorn.unirent.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, email, encodedPassword, Collections.singleton(Role.USER));
        return userRepository.save(newUser);
    }

    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}

