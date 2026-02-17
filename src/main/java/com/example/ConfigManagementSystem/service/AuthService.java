package com.example.ConfigManagementSystem.service;


import com.example.ConfigManagementSystem.model.AuthResponse;
import com.example.ConfigManagementSystem.model.LoginRequest;
import com.example.ConfigManagementSystem.model.User;
import com.example.ConfigManagementSystem.repo.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRespository userRepository;

    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate a dummy token or use a JWT utility
        String token = "mock-jwt-token-for-" + user.getEmail();

        return new AuthResponse(token, user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
