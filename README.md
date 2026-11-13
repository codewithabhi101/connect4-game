# Zip Puzzle and Connect 4

A daily number-path puzzle inspired by LinkedIn's Zip, plus real-time multiplayer Connect 4. Built with Java, Spring Boot and PostgreSQL.

**Play:** https://connect4-game-xdmd.onrender.com/zip.html
(The free server sleeps when idle, so the first load can take a minute.)

## Zip Puzzle
Start at 1, drag to connect the numbers in order, and fill every cell. The last number is the final cell.

- A new puzzle every day, the same for everyone (UTC)
- Streaks and a top 10 leaderboard saved in PostgreSQL
- Server-side solution check
- Dead-end warning when a route can no longer be finished
- One-tap result sharing

## Connect 4
Create a room, share the 4-letter code, and play live over WebSocket (STOMP).

## Tech
- Java 17, Spring Boot (REST, WebSocket, JPA)
- PostgreSQL (Neon)
- Plain HTML, CSS and JavaScript front end
- JUnit tests
- Docker, deployed on Render

## How the puzzle is made
1. A random path through every cell is built with a randomised depth-first search. The seed is the date, so the daily puzzle is the same for everyone.
2. Numbered checkpoints are placed along that path, so every puzzle has at least one solution.
3. The server regenerates the puzzle from the seed and validates the player's path. Each cell is used once, steps are adjacent, and numbers are visited in order.

## API
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/zip/daily` | Today's puzzle |
| GET | `/api/zip/random` | A random puzzle |
| POST | `/api/zip/solve` | Check a solution, save the time, update the streak |
| GET | `/api/zip/leaderboard` | Today's top 10 |
| GET | `/api/zip/streak` | A player's streak |
| POST | `/api/rooms` | Create a Connect 4 room |

## Run locally
1. Create a free PostgreSQL database (for example on Neon).
2. Create `src/main/resources/application-local.properties` (it is git-ignored):
   ```
   spring.datasource.url=jdbc:postgresql://HOST/DBNAME?sslmode=require
   spring.datasource.username=USER
   spring.datasource.password=PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Run:
   ```
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
4. Open http://localhost:8080/zip.html

## Run the tests
```
./mvnw test
```

## Known limitations
- The solve time is measured in the browser, so it could be faked. A fix is to time the puzzle on the server.
- Players are identified by a random id in the browser, without a login.