package dataaccess;

import chess.ChessGame;
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

    public int createGame(String gameName) {
        GameData game = new GameData(nextId++, null, null, gameName, new ChessGame());
        games.put(game.gameID(), game);
        return game.gameID();
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public ArrayList<GameData> listGames() {
        if (games.isEmpty()) {
            return new ArrayList<>();
        }
        return (ArrayList<GameData>) games.values();
    }

    public void updateGame(String playerColor, String username, GameData gameData) {
        GameData updated;
        if (Objects.equals(playerColor, "WHITE")) {
            updated = whiteUpdate(username,gameData);
        }
        else {
            updated = blackUpdate(username, gameData);
        }
        games.put(updated.gameID(), updated);
    }

    private GameData whiteUpdate(String username, GameData gameData) {
        return new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
    }

    private GameData blackUpdate(String username, GameData gameData) {
        return new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
    }

}
