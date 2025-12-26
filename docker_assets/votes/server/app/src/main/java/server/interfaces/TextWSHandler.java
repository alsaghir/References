package server.interfaces;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TextWSHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println(session.isOpen());
        sessions.add(session);
        System.out.println(sessions.size());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload());
        System.out.println(session.isOpen());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println(session.isOpen());
        Optional<WebSocketSession> sessionToRemove = sessions.stream().filter(s -> s.equals(session)).findFirst();
        if(sessionToRemove.isPresent()) {
            sessions.remove(session);
            System.out.println("removed");
        }
    }

    public List<WebSocketSession> getSessions() {
        return sessions;
    }



}