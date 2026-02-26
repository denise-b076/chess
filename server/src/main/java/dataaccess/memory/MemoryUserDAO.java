package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String,UserData> users = new HashMap<>();

    public void clearUsers() {
        users.clear();
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
        if (this.getUser(userData.username()) == null) {
            throw new DataAccessException("Error: unable to add " + userData.username() + " to users");
        }
        return userData;
    }

    public UserData getUser(String username) {
        return users.get(username);
    }
}
