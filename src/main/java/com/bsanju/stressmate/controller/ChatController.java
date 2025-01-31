package com.bsanju.stressmate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class ChatController {




    @PostMapping("/ask")
    public ResponseEntity<String> askAi(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String extractedToken = token.substring(7);

        try {
            return ResponseEntity.ok("AI Response to " + request.get("question") + " for user: " );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}
