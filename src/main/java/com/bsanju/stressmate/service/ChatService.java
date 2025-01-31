package com.bsanju.stressmate.service;

import com.bsanju.stressmate.model.ChatMessage;
import com.bsanju.stressmate.repository.ChatRepository;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ChatService {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions"; // DeepSeek API Endpoint
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY"); // Set in environment variables
    private final ChatRepository chatRepository;
    private final OkHttpClient httpClient;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.httpClient = new OkHttpClient();
    }

    public String getChatResponse(String userInput, String userId) throws Exception {
        String sentiment = analyzeSentiment(userInput);
        String prompt = "User is feeling " + sentiment + ". Respond kindly.\nUser: " + userInput + "\nBot:";

        String botResponse = callDeepSeekAPI(prompt);

        // Save chat history
        ChatMessage chat = new ChatMessage();
        chat.setUserId(userId);
        chat.setMessage(userInput);
        chat.setResponse(botResponse);
        chat.setSentiment(sentiment);
        chat.setTimestamp(LocalDateTime.now());
        chatRepository.saveChat(chat);

        return botResponse;
    }

    private String callDeepSeekAPI(String prompt) throws IOException {
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("model", "deepseek-chat"); // Use the latest DeepSeek model
        requestBodyJson.put("messages", new JSONObject()
                .put("role", "user")
                .put("content", prompt)
        );
        requestBodyJson.put("temperature", 0.7);
        requestBodyJson.put("max_tokens", 150);

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(), MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected response: " + response);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");
        }
    }

    private String analyzeSentiment(String text) {
        if (text.contains("bad") || text.contains("tired") || text.contains("stressed")) return "negative";
        if (text.contains("okay") || text.contains("not sure")) return "neutral";
        return "positive";
    }
}
