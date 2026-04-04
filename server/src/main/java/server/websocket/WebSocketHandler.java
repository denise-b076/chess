package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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
        AuthData authData = null;
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = userGameCommand.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData == null) {
                throw new BadRequestResponse("invalid gameID provided");
            }
            String username;
            authData = authDAO.getAuth(userGameCommand.getAuthToken());
            if (authData == null) {
                throw new UnauthorizedResponse();
            }
            username = authData.username();
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(username, session, gameID);
                case MAKE_MOVE -> makeMove(new Gson().fromJson(ctx.message(), MakeMoveCommand.class), gameID, username, session);
                case RESIGN -> resign(gameID, username);
                case LEAVE -> leave(gameID, username, session);
            }
        }
        catch (UnauthorizedResponse ex){
            var error = new ErrorMessage("Error: Unauthorized");
            if (authData == null) {
                session.getRemote().sendString(new Gson().toJson(error));
            }
            else {
                gameSessionManager.broadcastToGameOne(gameID, session, error);
            }
        }
        catch (BadRequestResponse ex) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ex.getMessage())));
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
            GameData gameData = gameDAO.getGame(gameID);
            var message = String.format("%s has joined the game", playerName);
            if (gameData.whiteUsername().equals(playerName)) {
                gameSessionManager.addSession(gameID, session, "WHITE");
                gameSessionManager.broadcastToGameOne(gameID, session, new LoadGameMessage(gameData, "WHITE"));
                message += " as white";
            } else if (gameData.blackUsername().equals(playerName)) {
                gameSessionManager.addSession(gameID, session, "BLACK");
                gameSessionManager.broadcastToGameOne(gameID, session, new LoadGameMessage(gameData, "BLACK"));
                message += " as black";
            }
            else {
                gameSessionManager.addSession(gameID, session, "WHITE");
                gameSessionManager.broadcastToGameOne(gameID, session, new LoadGameMessage(gameData, "WHITE"));
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
            if (gameData.blackUsername() != null && gameData.blackUsername().equals(playerName)) {
                gameDAO.clearUser(gameData,"BLACK");
            }
            else if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(playerName)) {
                gameDAO.clearUser(gameData,"WHITE");
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

    private void resign(int gameID, String playerName) throws Exception {
        try {
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.game();
            if (!gameData.blackUsername().equals(playerName) && !gameData.whiteUsername().equals(playerName)) {
                throw new Exception("cannot resign");
            }
            if (gameData.game().getGameStatus()) {
                throw new Exception("game already over");
            }
            game.setGameOver();
            GameData updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(updatedGame);
            var message = String.format("%s has resigned", playerName);
            var resignMessage = new NotificationMessage(message);
            gameSessionManager.broadcastToGameAll(gameID, resignMessage);
        }
        catch (Exception ex) {
            var message = "Error: " + ex.getMessage();
            throw new Exception(message);
        }
    }

    private void makeMove(MakeMoveCommand makeMoveCommand, int gameID, String playerName, Session session) throws Exception {
        try {
            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            ChessGame game = gameData.game();
            ChessGame.TeamColor color;
            String opposition;
            if (game.getGameStatus()) {
                throw new Exception("game is concluded, no more moves accepted");
            }
            if (!gameSessionManager.getGameConnections(gameID).getSessionInfo(session).color().equals(game.getTeamTurn().toString())) {
                throw new Exception("out of turn play attempted");
            }
            game.makeMove(makeMoveCommand.getMove());
            if (gameData.blackUsername() != null && gameData.blackUsername().equals(playerName)) {
                color = ChessGame.TeamColor.WHITE;
                opposition = gameData.whiteUsername();
            }
            else if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(playerName)) {
                color = ChessGame.TeamColor.BLACK;
                opposition = gameData.blackUsername();
            }
            else {
                throw new UnauthorizedResponse();
            }
            boolean isInCheckmate = game.isInCheckmate(color);
            boolean isInCheck = game.isInCheck(color);
            boolean isInStalemate = game.isInStalemate(color);
            String additional = "";
            String move = makeMoveCommand.getMove().toString();
            if (isInStalemate) {
                additional = String.format("%s is in stalemate. Game over!", opposition);
                game.setGameOver();
            }
            else if (isInCheckmate) {
                additional = String.format("%s is in checkmate. Game over!", opposition);
                game.setGameOver();
            }
            else if (isInCheck) {
                additional = String.format("%s is in check!", opposition);
            }
            GameData updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(updatedGame);
            var message = String.format("%s made the move %s", playerName, move);
            var moveMessage = new NotificationMessage(message);
            var loadMessage = new LoadGameMessage(updatedGame, "WHITE");
            gameSessionManager.broadcastToGameAll(gameID, loadMessage);
            gameSessionManager.broadcastToGameExclusive(gameID,session,moveMessage);
            if (!additional.isEmpty()) {
                var gameStateMessage = new NotificationMessage(additional);
                gameSessionManager.broadcastToGameAll(gameID, gameStateMessage);
            }
        }
        catch (UnauthorizedResponse ex) {
            throw new UnauthorizedResponse();
        }
        catch (Exception ex) {
//            var message = "Error: " + ex.getMessage();
            throw new Exception(ex.getMessage());
        }
    }
}
