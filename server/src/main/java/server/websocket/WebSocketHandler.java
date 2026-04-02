package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsCloseHandler, WsConnectHandler, WsMessageHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameSessionManager gameSessionManager = new GameSessionManager();

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
    public void handleMessage(WsMessageContext ctx) throws Exception {
        int gameID = -1;
        Session session = ctx.session;
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = userGameCommand.getGameID();
            switch (userGameCommand.getCommandType()) {
//                case CONNECT -> return;
//                case MAKE_MOVE -> return;
//                case RESIGN -> return;
                case LEAVE -> leave(userGameCommand, session);
            }
        }
        catch (UnauthorizedResponse ex){
            gameSessionManager.broadcastToGameOne(gameID, session, new ErrorMessage("Error: Unauthorized"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            gameSessionManager.broadcastToGameOne(gameID, session, new ErrorMessage("Error: " + ex.getMessage()));
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
            gameSessionManager.broadcastToGameExclusive(gameData.gameID(), session, leaveNotification);
            gameSessionManager.removeSession(gameData.gameID(), session);
            connections.remove(session);
        }
        catch (Exception ex) {
            var message = "Error: " + ex.getMessage();
            throw new Exception(message);
        }
    }
}
