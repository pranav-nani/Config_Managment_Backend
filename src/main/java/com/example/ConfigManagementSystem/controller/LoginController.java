package com.example.ConfigManagementSystem.controller;

import com.example.ConfigManagementSystem.model.AuthResponse;
import com.example.ConfigManagementSystem.model.LoginRequest;
import com.example.ConfigManagementSystem.model.User;
import com.example.ConfigManagementSystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try{
            User savedUser = authService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }
}
