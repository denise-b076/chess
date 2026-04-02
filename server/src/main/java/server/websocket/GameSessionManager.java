package server.websocket;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class GameSessionManager {
    public final HashMap<Integer, ConnectionManager> gameSessions = new HashMap<>();

    public GameSessionManager() {
    }

    public void addSession(int gameID, Session session) {
        if (!gameSessions.containsKey(gameID)) {
            gameSessions.put(gameID, new ConnectionManager());
        }
        gameSessions.get(gameID).add(session);
    }

    public void removeSession(int gameID, Session session) {
        gameSessions.get(gameID).remove(session);
    }

    public void endGame(int gameID) {
        gameSessions.get(gameID).endGame();
    }

    public boolean getGameStatus(int gameID) {
        return gameSessions.get(gameID).getGameStatus();
    }

    public void broadcastToGameExclusive(int gameID, Session session, ServerMessage serverMessage) throws IOException {
        gameSessions.get(gameID).broadcastToAllExcept(session, serverMessage);
    }

    public void broadcastToGameAll(int gameID, ServerMessage serverMessage) throws IOException {
        gameSessions.get(gameID).broadcastToAll(serverMessage);
    }

    public void broadcastToGameOne(int gameID, Session session, ServerMessage serverMessage) throws IOException {
        gameSessions.get(gameID).broadcastToOne(session, serverMessage);
    }
}
