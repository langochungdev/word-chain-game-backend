package chat.controller;

import chat.service.FirebaseChatRepository;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RoomController {
    private final SimpMessagingTemplate template;
    private final FirebaseDatabase db;
    private final FirebaseChatRepository chatRepo;

    public RoomController(SimpMessagingTemplate template, FirebaseDatabase db,FirebaseChatRepository chatRepo) {
        this.template = template;
        this.db = db;
        this.chatRepo = chatRepo;
    }

    @MessageMapping("/room.update")
    public void updateRoom(@Payload Map<String, Object> info) {
        if (info == null) return;
        Object roomIdObj = info.get("roomId");
        if (roomIdObj == null) return;
        String roomId = String.valueOf(roomIdObj).trim();
        if (roomId.isEmpty()) return;
        
        Object typeObj = info.get("type");
        if (typeObj != null && "reset".equals(String.valueOf(typeObj))) {
            chatRepo.deleteAllByRoomId(roomId);
        }
        // Lưu phần cần thiết (không đụng vào scores/type/winner để khỏi mất)
        Map<String, Object> data = new HashMap<>();
        data.put("roomId", roomId);
        data.put("targetScore", info.get("targetScore"));
        data.put("players", info.get("players"));
        db.getReference("rooms").child(roomId).setValueAsync(data);

        // Broadcast nguyên payload (giữ type, scores, winner, origin...)
        template.convertAndSend("/topic/room-info." + roomId, info);
    }
}
