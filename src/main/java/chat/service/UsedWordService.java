package chat.service;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsedWordService {
    private final Map<String, Set<String>> usedWords = new ConcurrentHashMap<>();

    public synchronized boolean addIfNew(String roomId, String word) {
        return usedWords.computeIfAbsent(roomId, k -> new HashSet<>()).add(word.toLowerCase());
    }

    public void clear(String roomId) {
        usedWords.remove(roomId);
    }
}
