package com.github.alsaghir.server.interfaces;

import com.github.alsaghir.server.domain.Value;
import com.github.alsaghir.server.domain.ValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.NullableUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/values")
public class AppController {

    private final StringRedisTemplate rs;
    private final ValueRepository valueRepository;


    @GetMapping("/all")
    public List<String> allValues() {
        return valueRepository.findAll().stream().map(Value::getValue).toList();
    }

    @MessageMapping
    @GetMapping("/current")
    public Map<Object, Object> currentValues() {
        return Optional.of(rs.opsForHash().entries("values")).orElse(Collections.emptyMap());
    }

    @PostMapping
    public ResponseEntity<String> currentValues(@RequestBody Map<String, String> req) {
        assert req.get("index") != null;
        String index = req.get("index");

        if (Integer.parseInt(index) > 40)
            return ResponseEntity.unprocessableEntity().build();

        rs.opsForHash().put("values", index, "Nothing yet!");
        rs.convertAndSend("insert", index);
        valueRepository.save(Value.builder().value(index).build());

        return ResponseEntity.ok("""
                { "working": true }
                """);
    }

}
