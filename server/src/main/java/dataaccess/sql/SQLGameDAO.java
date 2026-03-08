package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {

    }

    public void clearGames() throws DataAccessException {

    }

    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    public void updateGame(String playerColor, String username, GameData gameData) throws DataAccessException {

    }
}
