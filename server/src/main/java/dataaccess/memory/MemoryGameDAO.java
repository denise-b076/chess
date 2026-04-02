package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {

    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void clearGames() {
        games.clear();
    }

    public int createGame(String gameName) throws DataAccessException {
        GameData game = new GameData(nextId++, null, null, gameName, new ChessGame());
        games.put(game.gameID(), game);
        if (getGame(game.gameID()) == null) {
            throw new DataAccessException("Error: could not create new game");
        }
        return game.gameID();
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public ArrayList<GameData> listGames() {
        if (games.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<GameData> listOfGames = new ArrayList<>();
        for (int id : games.keySet()) {
            listOfGames.add(games.get(id));
        }
        return listOfGames;
    }

    public void joinGame(String playerColor, String username, GameData gameData) throws BadRequestResponse {
        GameData updated;
        if (Objects.equals(playerColor, "WHITE")) {
            updated = whiteUpdate(username,gameData);
        }
        else if (Objects.equals(playerColor, "BLACK")) {
            updated = blackUpdate(username, gameData);
        }
        else {
            throw new BadRequestResponse("Error: invalid playerColor");
        }
        games.put(updated.gameID(), updated);
    }

    public void updateGame(GameData gameData) {
    }

    public void clearUser(GameData gameData, String color) {
    }

    private GameData whiteUpdate(String username, GameData gameData) {
        if (gameData.whiteUsername() != null) {
            throw new ForbiddenResponse("Error: already taken");
        }
        return new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
    }

    private GameData blackUpdate(String username, GameData gameData) {
        if (gameData.blackUsername() != null) {
            throw new ForbiddenResponse("Error: already taken");
        }
        return new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
    }

}
