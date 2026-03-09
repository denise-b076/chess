package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {

    }

    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
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

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}
