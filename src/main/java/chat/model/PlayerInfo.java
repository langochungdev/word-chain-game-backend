package chat.model;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerInfo {
    private String id;
    private String name;
    private String core;
}