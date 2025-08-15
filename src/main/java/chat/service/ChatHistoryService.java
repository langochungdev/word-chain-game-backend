package chat.service;
import chat.model.ChatMessage;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatHistoryService {
    private final Map<String, List<ChatMessage>> history = new ConcurrentHashMap<>();

    public void add(String roomId, ChatMessage msg) {
        history.computeIfAbsent(roomId, k -> new ArrayList<>()).add(msg);
    }

    public List<ChatMessage> get(String roomId) {
        return history.getOrDefault(roomId, Collections.emptyList());
    }
    public void clear(String roomId) {
        history.remove(roomId);
    }
}

