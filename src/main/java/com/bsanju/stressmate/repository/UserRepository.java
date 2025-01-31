package com.bsanju.stressmate.repository;

import com.bsanju.stressmate.model.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    private final Firestore firestore;

    public UserRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection("users");
        return users.document(email) // Use document ID directly as email
                .get()
                .get()
                .exists() ? Optional.ofNullable(users.document(email).get().get().toObject(User.class)) : Optional.empty();
    }

    public void save(User user) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(user.getEmail()).set(user).get(); // ✅ Use email as the document ID
    }
}
