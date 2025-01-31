package com.bsanju.stressmate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collections;

@Service
public class DeepSeekService {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    @Value("${deepseek.api.key}") // Read from application.properties
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getChatResponse(String userMessage) {
        try {
            // Construct request body
            String requestBody = objectMapper.writeValueAsString(Collections.singletonMap("messages",
                    Collections.singletonList(Collections.singletonMap("content", userMessage))
            ));

            // Build HTTP request
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .build();

            // Execute request
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                return jsonResponse.path("choices").get(0).path("message").path("content").asText();
            } else {
                return "Error: " + response.message();
            }
        } catch (IOException e) {
            return "Exception: " + e.getMessage();
        }
    }
}
