package chat.controller;
import chat.model.ChatMessage;
import chat.service.ChatHistoryService;
import chat.service.UsedWordService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatHistoryService chatHistoryService;
    private final UsedWordService usedWordService;
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatHistoryService chatHistoryService, UsedWordService usedWordService) {
        this.messagingTemplate = messagingTemplate;
        this.chatHistoryService = chatHistoryService;
        this.usedWordService = usedWordService;
    }

    @MessageMapping("/chat.send")
    public void onMessage(@Valid @Payload ChatMessage message) {
        message.setTimestamp(Instant.now());
        String word = message.getContent().trim().toLowerCase();
        if (!usedWordService.addIfNew(message.getRoomId(), word)) {
            // gửi lỗi chung, kèm origin để client tự xử lý
            Map<String, Object> err = Map.of(
                    "type", "error",
                    "origin", message.getSenderId(),
                    "msg", "Từ này đã được sử dụng trước đó"
            );
            messagingTemplate.convertAndSend("/topic/room-info." + message.getRoomId(), err);
            return;
        }
        chatHistoryService.add(message.getRoomId(), message);
        messagingTemplate.convertAndSend("/topic/room." + message.getRoomId(), message);
    }

    @MessageMapping("/chat.history")
    public void sendHistory(@Payload Map<String, String> payload) {
        String roomId = payload.get("roomId");
        List<ChatMessage> list = chatHistoryService.get(roomId);
        messagingTemplate.convertAndSend("/topic/room-history." + roomId, list);
    }

}
