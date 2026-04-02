package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

public record SessionInfo(Session session, String color) {
}
