package com.bsanju.stressmate.dto;

public class LoginRequest {
    private String firebaseToken;  // Ensure this field exists

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
