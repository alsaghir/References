package server.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import server.domain.Votes;
import server.domain.VotesRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AppController {

    private final StringRedisTemplate rs;
    private final VotesRepository repository;

    private final ObjectMapper objectMapper;
    private final Map<String, Integer> votes = new HashMap<>();

    private final TextWSHandler textWSHandler;

    @GetMapping("/votes")
    public Map<String, Integer> allValues() {
        String yesVotes = rs.opsForHash().get("yes", "yes") == null ? "0" : (String) rs.opsForHash().get("yes", "yes");
        String noVotes = rs.opsForHash().get("no", "no") == null ? "0" : (String) rs.opsForHash().get("no", "no");
        votes.put("yes", Integer.parseInt(yesVotes));
        votes.put("no", Integer.parseInt(noVotes));
        return votes;
    }

    @PostMapping("/votes")
    public ResponseEntity<String> currentValues(@RequestBody List<String> req) throws IOException {

        if (req.size() == 0 || (!req.get(0).equals("yes") && !req.get(0).equals("no")))
            throw new IllegalStateException("Either yes or no should be sent as request body json array [\"yes\"]");

        Votes votes = repository.findTopBy().orElseGet(Votes::new);

        votes.setYes(req.get(0).equals("yes") ? votes.getYes() + 1 : votes.getYes());
        votes.setNo(req.get(0).equals("no") ? votes.getNo() + 1 : votes.getNo());

        repository.save(votes);

        rs.convertAndSend("insert", objectMapper.writeValueAsString(votes));

        if (!CollectionUtils.isEmpty(textWSHandler.getSessions())
                && textWSHandler.getSessions().stream().anyMatch(WebSocketSession::isOpen)) {
            for (WebSocketSession session : textWSHandler.getSessions()) {
                session.sendMessage(new TextMessage("fire"));
            }
        }

        return ResponseEntity.ok().build();
    }

}
