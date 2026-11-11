package com.abhi.connect4.room;

import java.security.SecureRandom;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public Room create() {
        while (true) {
            String code = randomCode();
            Room room = new Room(code);
            if (rooms.putIfAbsent(code, room) == null) {
                return room;
            }
        }
    }

    public Room get(String code) {
        Room room = rooms.get(code.toUpperCase());
        if (room == null) {
            throw new NoSuchElementException("Room not found");
        }
        return room;
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}