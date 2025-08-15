package chat.controller;
import chat.service.ChatHistoryService;
import chat.service.UsedWordService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;

@Controller
public class RoomController {
    private final SimpMessagingTemplate template;
    private final ChatHistoryService chatHistoryService;
    private final UsedWordService usedWordService;

    public RoomController(SimpMessagingTemplate template,
                          ChatHistoryService chatHistoryService,
                          UsedWordService usedWordService) {
        this.template = template;
        this.chatHistoryService = chatHistoryService;
        this.usedWordService = usedWordService;
    }

    @MessageMapping("/room.update")
    public void updateRoom(@Payload Map<String, Object> info) {
        if (info == null) return;
        Object roomIdObj = info.get("roomId");
        if (roomIdObj == null) return;
        String roomId = String.valueOf(roomIdObj).trim();
        if (roomId.isEmpty()) return;

        Object typeObj = info.get("type");
        if (typeObj != null && "reset".equalsIgnoreCase(String.valueOf(typeObj))) {
            chatHistoryService.clear(roomId);
            usedWordService.clear(roomId);
        }

        Object playersObj = info.get("players");
        if (!(playersObj instanceof List) || ((List<?>) playersObj).isEmpty()) {
            chatHistoryService.clear(roomId);
            usedWordService.clear(roomId);
        }
        template.convertAndSend("/topic/room-info." + roomId, info);
    }
}
