package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {

    }

    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (game_name, white_username, black_username, game_json) VALUES (?, ?, ?, ?)";
        return DatabaseManager.executeUpdate(statement, gameName, null, null, new ChessGame());
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game_id, game_name, white_username, black_username, game_json FROM games WHERE game_id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUser = rs.getString("white_username");
                        String blackUser = rs.getString("black_username");
                        String gameName = rs.getString("game_name");
                        ChessGame game = new Gson().fromJson(rs.getString("game_json"), ChessGame.class);
                        return new GameData(gameID, whiteUser, blackUser, gameName, game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
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
                        int gameID = rs.getInt("game_id");
                        String whiteUser = rs.getString("white_username");
                        String blackUser = rs.getString("black_username");
                        String gameName = rs.getString("game_name");
                        ChessGame game = new Gson().fromJson(rs.getString("game_json"), ChessGame.class);
                        result.add(new GameData(gameID, whiteUser, blackUser, gameName, game));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return result;
    }

    public void joinGame(String playerColor, String username, GameData gameData) throws DataAccessException, BadRequestResponse {
        String statement;
        if (playerColor.equals("WHITE")) {
            whiteUpdate(gameData);
            statement = "UPDATE games SET white_username = ? WHERE game_id = ?";
        }
        else if (playerColor.equals("BLACK")) {
            blackUpdate(gameData);
            statement = "UPDATE games SET black_username = ? WHERE game_id = ?";
        }
        else {
            throw new BadRequestResponse("Error: invalid color");
        }
        DatabaseManager.executeUpdate(statement, username, gameData.gameID());
    }

    public void updateGame(GameData gameData) throws DataAccessException, BadRequestResponse {
        var statement = "UPDATE games SET game_json = ? WHERE game_id = ?";
        if (getGame(gameData.gameID()) == null) {
            throw new BadRequestResponse("Error: bad request");
        }
        DatabaseManager.executeUpdate(statement, gameData.game(), gameData.gameID());
    }


    private void whiteUpdate(GameData gameData) throws DataAccessException {
        if (getGame(gameData.gameID()).whiteUsername() != null) {
            throw new ForbiddenResponse("Error: already taken");
        }
    }

    private void blackUpdate(GameData gameData) throws DataAccessException {
        if (getGame(gameData.gameID()).blackUsername() != null) {
            throw new ForbiddenResponse("Error: already taken");
        }
    }
}
