package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {

    }

    public void clearUsers() throws DataAccessException {

    }

    public UserData createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (user, password, email) VALUES (?, ?, ?)";
        String hashedPass = passwordHash(userData.password());
        executeUpdate(statement, userData.username(), hashedPass, userData.email());
        return userData;
    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    private String passwordHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void executeUpdate(String statement, String... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setString(i + 1, params[i]);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}
