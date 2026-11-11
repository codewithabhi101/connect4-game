package com.abhi.connect4.room;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService rooms;
    private final SimpMessagingTemplate messaging;

    public RoomController(RoomService rooms, SimpMessagingTemplate messaging) {
        this.rooms = rooms;
        this.messaging = messaging;
    }

    @PostMapping
    public Dtos.CreateResponse create() {
        return new Dtos.CreateResponse(rooms.create().state().code());
    }

    @PostMapping("/{code}/join")
    public Dtos.JoinResponse join(@PathVariable String code, @RequestBody Dtos.JoinRequest req) {
        Room room = rooms.get(code);
        int number = room.join(req.playerId());
        Dtos.GameState state = room.state();
        messaging.convertAndSend("/topic/rooms/" + state.code(), state);
        return new Dtos.JoinResponse(state.code(), number, state);
    }

    @GetMapping("/{code}")
    public Dtos.GameState get(@PathVariable String code) {
        return rooms.get(code).state();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> badRequest(RuntimeException e) {
        return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
    }
}