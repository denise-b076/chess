package dataaccess;

import model.UserData;

public interface UserDAO {

    void clearUsers() throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

}
