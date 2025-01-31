package com.bsanju.stressmate.controller;

import com.bsanju.stressmate.model.User;
import com.bsanju.stressmate.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a new user (this remains the same)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String message = authService.register(user.getEmail(), user.getPassword(), user.getRole());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // Handle login (No JWT, just Firebase token verification)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String firebaseToken) {
        try {
            String message = authService.login(firebaseToken); // Firebase token validation
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}
