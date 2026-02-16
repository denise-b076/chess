package dataaccess;

import model.GameData;

public interface GameDAO {
    void clearGames() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    GameData[] listGames() throws DataAccessException;

    void updateGame(String playerColor, GameData gameData) throws DataAccessException;
}
