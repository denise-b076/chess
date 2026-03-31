package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsCloseHandler, WsConnectHandler, WsMessageHandler {

    private final ConnectionManager connections = new ConnectionManager();

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
//                case CONNECT -> return;
//                case MAKE_MOVE -> return;
//                case RESIGN -> return;
                case LEAVE -> leave(userGameCommand, ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String playerName, Session session) {
//        connections.add(session);
//        var message = String.format("")
    }

    private void leave(UserGameCommand userGameCommand, Session session) throws Exception {
        try {
            String playerName = authDAO.getAuth(userGameCommand.getAuthToken()).username();
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
            if (gameData.blackUsername().equals(playerName)) {
                gameDAO.joinGame("BLACK", null, gameData);
            }
            else if (gameData.whiteUsername().equals(playerName)) {
                gameDAO.joinGame("WHITE", null, gameData);
            }
            var message = String.format("%s has left the game", playerName);
            var leaveNotification = new NotificationMessage(message);
            connections.broadcast(session, leaveNotification);
            connections.remove(session);
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
