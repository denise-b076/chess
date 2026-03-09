package dataaccess;

import dataaccess.sql.DatabaseManager;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;


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
                """
        };
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : truncateStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
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
    void createUserNullField() {
        UserData failure = new UserData(null, "oh", "no");
        assertThrows(DataAccessException.class, () -> sqlUserDAO.createUser(failure));
    }

    @Test
    void getUserNotInDB() throws DataAccessException {
        assertNull(sqlUserDAO.getUser("ironman"));
    }
}
