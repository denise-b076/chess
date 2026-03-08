package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
            game_id int NOT NULL AUTO_INCREMENT,
            game_name varchar(256) NOT NULL,
            white_username varchar(256),
            black_username varchar(256),
            game_json TEXT DEFAULT NULL,
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
