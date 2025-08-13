package chat.controller;
import chat.model.ChatMessage;
import chat.service.FirebaseChatRepository;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final FirebaseChatRepository repo;

    public ChatController(SimpMessagingTemplate messagingTemplate, FirebaseChatRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    // Client send đến /app/chat.send
    @MessageMapping("/chat.send")
    public void onMessage(@Valid @Payload ChatMessage message) {
        message.setTimestamp(Instant.now());
        repo.saveMessage(message); // async
        messagingTemplate.convertAndSend("/topic/room." + message.getRoomId(), message);
    }
}
