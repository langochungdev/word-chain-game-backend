package chat.model;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomInfo {
    private String roomId;
    private String type;
    private Integer targetScore;
    private String origin;
    private List<PlayerInfo> players;
    private Map<String, Integer> scores;
    private PlayerInfo winner;
}