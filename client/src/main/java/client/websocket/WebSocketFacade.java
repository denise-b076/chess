package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(int port, NotificationHandler notificationHandler) throws Exception {
        try {
            String url = "ws://localhost:" + port;
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage typeChecker = new Gson().fromJson(message, ServerMessage.class);
                    ServerMessage serverMessage;
                    try {
                        switch (typeChecker.getServerMessageType()) {
                            case NOTIFICATION ->
                                    serverMessage = new Gson().fromJson(message, NotificationMessage.class);
                            case ERROR -> serverMessage = new Gson().fromJson(message, ErrorMessage.class);
                            case LOAD_GAME -> serverMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            default -> throw new Exception("Error: unknown server message type");
                        }
                        notificationHandler.notify(serverMessage);
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws Exception {
        try{
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        }
        catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        try{
            var makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        }
        catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws Exception {
        try {
            var leaveGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveGameCommand));
        }
        catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws Exception {
        try {
            var resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCommand));
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
