package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.BadRequestResponse;
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
        var statement = "INSERT INTO games (game_name, white_username, black_username, game_json) VALUES (?, ?, ?, ?)";
        return executeUpdate(statement, gameName, null, null, new ChessGame());
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game_id, game_name, white_username, black_username, game_json FROM games WHERE game_id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ChessGame game = new Gson().fromJson(rs.getString("game_json"), ChessGame.class);
                        return new GameData(rs.getInt("game_id"), rs.getString("white_username"), rs.getString("black_username"), rs.getString("game_name"), game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game_id, game_name, white_username, black_username, game_json FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ChessGame game = new Gson().fromJson(rs.getString("game_json"), ChessGame.class);
                        result.add(new GameData(rs.getInt("game_id"), rs.getString("white_username"), rs.getString("black_username"), rs.getString("game_name"), game));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return result;
    }

    public void updateGame(String playerColor, String username, GameData gameData) throws DataAccessException, BadRequestResponse {
        String statement;
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE games SET white_username = ? WHERE game_id = ?";
            executeUpdate(statement, username, gameData.gameID());
        }
        else if (playerColor.equals("BLACK")) {
            statement = "UPDATE games SET black_username = ? WHERE game_id = ?";
            executeUpdate(statement, username, gameData.gameID());
        }
        else {
            statement = "UPDATE games SET game_json = ? WHERE game_id = ?";
            if (getGame(gameData.gameID()) == null) {
                throw new BadRequestResponse("Error: bad request");
            }
            executeUpdate(statement, gameData.game(), gameData.gameID());
        }
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
                    else if (param instanceof ChessGame p) {
                        ps.setString(i + 1, new Gson().toJson(p));
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
