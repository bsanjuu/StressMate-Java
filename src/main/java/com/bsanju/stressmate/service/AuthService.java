package com.bsanju.stressmate.service;

import com.bsanju.stressmate.model.User;
import com.bsanju.stressmate.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Register User in Firestore (Firestore Document ID is Email)
    public String register(String email, String password, String role) {
        try {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("User already exists");
            }

            User newUser = new User();
            newUser.setEmail(email); // 👈 Email as Firestore document ID
            newUser.setPassword(password); // Storing plain password (usually hashed before storing)
            newUser.setRole(role);

            // 🔹 Save user in Firestore
            userRepository.save(newUser);

            return "User registered successfully!";
        } catch (ExecutionException | InterruptedException e) {
            // Handle Firebase or Firestore asynchronous exceptions
            throw new RuntimeException("Error during registration: " + e.getMessage());
        }
    }

    // ✅ Login using Firebase Token (No JWT)
    public String login(String firebaseToken) {
        try {
            // 🔹 Verify Firebase token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            // 🔹 Extract user email from Firebase token
            String email = decodedToken.getEmail();

            // 🔹 Check if user exists in Firestore
            if (userRepository.findByEmail(email).isEmpty()) {
                throw new RuntimeException("User not found in database");
            }

            // 🔹 Return success message (no JWT required)
            return "Login successful for user: " + email;

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid Firebase token: " + e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            // Handle Firebase or Firestore asynchronous exceptions
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
    }
}
