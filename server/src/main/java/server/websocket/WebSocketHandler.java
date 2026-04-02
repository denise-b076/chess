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
            String username = authDAO.getAuth(userGameCommand.getAuthToken()).username();
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(username, session, gameID);
//                case MAKE_MOVE -> return;
//                case RESIGN -> return;
                case LEAVE -> leave(gameID, username, session);
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

    private void connect(String playerName, Session session, int gameID) throws Exception {
        try {
            gameSessionManager.addSession(gameID, session);
            GameData gameData = gameDAO.getGame(gameID);
            var message = String.format("%s has joined the game", playerName);
            if (gameData.whiteUsername().equals(playerName)) {
                message += " as white";
            } else if (gameData.blackUsername().equals(playerName)) {
                message += " as black";
            }
            var connectNotification = new NotificationMessage(message);
            gameSessionManager.broadcastToGameExclusive(gameID, session, connectNotification);
        }
        catch (Exception ex) {
            var message = "Error: " + ex.getMessage();
            throw new Exception(message);
        }
    }

    private void leave(int gameID, String playerName, Session session) throws Exception {
        try {
            GameData gameData = gameDAO.getGame(gameID);
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
        }
        catch (Exception ex) {
            var message = "Error: " + ex.getMessage();
            throw new Exception(message);
        }
    }
}
