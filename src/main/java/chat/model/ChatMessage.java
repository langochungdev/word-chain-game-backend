package chat.model;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @NotBlank
    private String roomId;
    @NotBlank
    private String senderId;
    @NotBlank
    private String content;
    private Instant timestamp;
}
