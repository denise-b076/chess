package dataaccess;

import model.AuthData;

public interface AuthDAO {

    AuthData createAuth(AuthData authData) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(AuthData authData) throws DataAccessException;

}
