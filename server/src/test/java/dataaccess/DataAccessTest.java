package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.sql.DatabaseManager;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DataAccessTest {
    static SQLUserDAO sqlUserDAO;
    static SQLGameDAO sqlGameDAO;
    static SQLAuthDAO sqlAuthDAO;

    @BeforeAll
    static void startDatabase() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
            user varchar(256) NOT NULL,
            password varchar(256) NOT NULL,
            email varchar(256) NOT NULL,
            PRIMARY KEY (user)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
            game_id int NOT NULL AUTO_INCREMENT,
            game_name varchar(256) NOT NULL,
            white_username varchar(256),
            black_username varchar(256),
            game_json TEXT DEFAULT NULL,
            PRIMARY KEY(game_id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
            token varchar(256) NOT NULL,
            user varchar(256) NOT NULL,
            PRIMARY KEY (token)
            );
            """
        };
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

    @BeforeEach
    void reset() throws DataAccessException {
        sqlUserDAO = new SQLUserDAO();
        sqlAuthDAO = new SQLAuthDAO();
        sqlGameDAO = new SQLGameDAO();
        String[] truncateStatements = {
                """
                TRUNCATE user;
                """,
                """
                TRUNCATE auth;
                """,
                """
                TRUNCATE games;
                """,
                """
                ALTER TABLE games AUTO_INCREMENT = 1;
                """,
                """
                INSERT INTO user (user, password, email) VALUES ('user', 'pass', 'email');
                """,
                """
                INSERT INTO auth (token, user) VALUES ('token', 'user');
                """,
                """
                INSERT INTO games (game_name, white_username, black_username, game_json) VALUES ('hello', 'white', 'black', ?);
                """
        };
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : truncateStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    if (statement.equals("INSERT INTO games (game_name, white_username, black_username, game_json) VALUES ('hello', 'white', 'black', ?);\n")) {
                        String game_json = new Gson().toJson(new ChessGame());
                        preparedStatement.setString(1, game_json);
                    }
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException(String.format("Unable to truncate tables: %s", ex.getMessage()));
        }
    }
    @Test
    void createUserSuccess() throws DataAccessException {
        UserData expected = new UserData("bat", "man", "wayne");
        UserData actual = sqlUserDAO.createUser(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData expected = new UserData("user", "pass", "email");
        assertEquals(expected, sqlUserDAO.getUser("user"));
    }

    @Test
    void clearUsersSuccess() throws DataAccessException {
        sqlUserDAO.clearUsers();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    assertFalse(rs.next());
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Test
    void clearGamesSuccess() throws DataAccessException {
        sqlGameDAO.clearGames();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    assertFalse(rs.next());
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        int game_id = sqlGameDAO.createGame("testing");
        assertEquals(2, game_id);
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameData expected = new GameData(1, "white", "black", "hello", new ChessGame());
        GameData actual = sqlGameDAO.getGame(1);
        assertEquals(expected, actual);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        sqlGameDAO.createGame("world");
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData(1, "white", "black", "hello", new ChessGame()));
        expected.add(new GameData(2, null, null, "world", new ChessGame()));
        ArrayList<GameData> actual = sqlGameDAO.listGames();
        assertEquals(expected, actual);
    }

    @Test
    void createUserNullField() {
        UserData failure = new UserData(null, "oh", "no");
        assertThrows(DataAccessException.class, () -> sqlUserDAO.createUser(failure));
    }

    @Test
    void getUserNotInDB() throws DataAccessException {
        assertNull(sqlUserDAO.getUser("ironman"));
    }

    @Test
    void createGameNoName() {
        assertThrows(DataAccessException.class, () -> sqlGameDAO.createGame(null));
    }

    @Test
    void getGameNotExist() throws DataAccessException {
        assertNull(sqlGameDAO.getGame(2));
    }

    @Test
    void listGamesFailure() {
        //WE NEED SOMETHING HERE. CONSULT WITH THE TA's!!!!!!!!!!!!
    }
}
