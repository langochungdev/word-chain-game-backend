package chat.service;

import chat.model.ChatMessage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseChatRepository {
    private final DatabaseReference rootRef;

    public FirebaseChatRepository(FirebaseApp firebaseApp) {
        this.rootRef = FirebaseDatabase.getInstance(firebaseApp).getReference();
    }

    public CompletableFuture<Void> saveMessage(ChatMessage msg) {
        DatabaseReference ref = rootRef.child("messages")
                .child(msg.getRoomId())
                .push();

        Map<String, Object> data = new HashMap<>();
        data.put("roomId", msg.getRoomId());
        data.put("senderId", msg.getSenderId());
        data.put("content", msg.getContent());
        data.put("timestamp", ServerValue.TIMESTAMP);

        CompletableFuture<Void> f = new CompletableFuture<>();
        ref.setValue(data, (error, ref1) -> {
            if (error != null) f.completeExceptionally(error.toException());
            else f.complete(null);
        });
        return f;
    }

    public CompletableFuture<Void> deleteAllByRoomId(String roomId) {
        DatabaseReference ref = rootRef.child("messages").child(roomId);
        CompletableFuture<Void> f = new CompletableFuture<>();
        ref.removeValue((error, ref1) -> {
            if (error != null) f.completeExceptionally(error.toException());
            else f.complete(null);
        });
        return f;
    }
}
