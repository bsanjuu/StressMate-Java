package com.bsanju.stressmate.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public class User {

    @DocumentId
    private String email; // Email is the Firestore document ID

    @PropertyName("role")
    private String role;

    @PropertyName("password")
    private String password;

    // ✅ Empty constructor for Firestore
    public User() {}

    // ✅ Constructor
    public User(String email, String role, String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // ✅ Getters
    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    // ✅ Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ Convert Firestore Document to User Object
    public static User fromDocument(QueryDocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("role"),
                doc.getString("password") // Ensure this field exists in Firestore
        );
    }
}
