package com.abhi.connect4.room;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final RoomService rooms;
    private final SimpMessagingTemplate messaging;

    public GameController(RoomService rooms, SimpMessagingTemplate messaging) {
        this.rooms = rooms;
        this.messaging = messaging;
    }

    @MessageMapping("/rooms/{code}/move")
    public void move(@DestinationVariable String code, Dtos.MoveRequest req) {
        try {
            Room room = rooms.get(code);
            room.move(req.playerId(), req.col());
            messaging.convertAndSend("/topic/rooms/" + code.toUpperCase(), room.state());
        } catch (RuntimeException e) {
            messaging.convertAndSend("/topic/errors/" + req.playerId(), e.getMessage());
        }
    }
}