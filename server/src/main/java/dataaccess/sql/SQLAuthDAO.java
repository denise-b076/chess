package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {

    }

    public void clearAuths() throws DataAccessException {
        var statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (token, user) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE token = ?";
        if (authData.authToken() == null) {
            throw new DataAccessException("Error: authToken is null");
        }
        DatabaseManager.executeUpdate(statement, authData.authToken());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT token, user FROM auth WHERE token=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("token"), rs.getString("user"));
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }
}
