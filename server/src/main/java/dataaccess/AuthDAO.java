package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clearAuths() throws DataAccessException;

    AuthData createAuth(AuthData authData) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(AuthData authData) throws DataAccessException;

}
