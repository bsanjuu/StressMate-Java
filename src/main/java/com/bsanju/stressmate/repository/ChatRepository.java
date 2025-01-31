package com.bsanju.stressmate.repository;

import com.bsanju.stressmate.model.ChatMessage;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ExecutionException;

@Repository
public class ChatRepository {
    private final Firestore firestore;

    public ChatRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveChat(ChatMessage chat) throws ExecutionException, InterruptedException {
        firestore.collection("chats").add(chat).get();
    }
}
