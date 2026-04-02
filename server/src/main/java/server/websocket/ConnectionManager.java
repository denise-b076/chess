package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Session, SessionInfo> connections = new ConcurrentHashMap<>();
//    public boolean gameOver = false;

    public SessionInfo getSessionInfo(Session session) {
        return connections.get(session);
    }

    public void add(Session session, String color) {
        connections.put(session, new SessionInfo(session, color));
    }

    public void remove(Session session) {
        connections.remove(session);
    }

//    public boolean getGameStatus() {
//        return gameOver;
//    }

//    public void endGame() {
//        gameOver = true;
//    }

    private LoadGameMessage colorConversion(SessionInfo sessionInfo, LoadGameMessage loadGameMessage) {
        if (sessionInfo.color().equals("BLACK")) {
            return new LoadGameMessage(loadGameMessage.getGame(), "BLACK");
        }
        return loadGameMessage;
    }

    public void broadcastToAll(ServerMessage serverMessage) throws IOException {
        String msg = new Gson().toJson(serverMessage);
        for (Session c : connections.keySet()) {
            if (c.isOpen()) {
                if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    serverMessage = colorConversion(connections.get(c), (LoadGameMessage) serverMessage);
                }
                c.getRemote().sendString(msg);
            }
        }
    }

    public void broadcastToAllExcept(Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = new Gson().toJson(serverMessage);
        for (Session c : connections.keySet()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastToOne(Session session, ServerMessage serverMessage) throws IOException {
        String msg = new Gson().toJson(serverMessage);
        for (Session c : connections.keySet()) {
            if (c.isOpen()) {
                if (c.equals(session)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
