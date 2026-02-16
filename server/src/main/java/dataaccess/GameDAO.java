package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGames() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(String playerColor, String username, GameData gameData) throws DataAccessException;
}
