package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();

    public void clearAuths() {
        auths.clear();
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        auths.put(authData.authToken(), authData);
        if (this.getAuth(authData.authToken()) == null) {
            throw new DataAccessException("Error: unable to add new authData to auths");
        }
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        auths.remove(authData.authToken());
        if (this.getAuth(authData.authToken()) != null) {
            throw new DataAccessException("Error: unable to delete authData from auths");
        }
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
}
