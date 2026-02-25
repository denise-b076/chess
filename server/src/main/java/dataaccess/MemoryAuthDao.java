package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDao implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();

    public void clearAuths() {
        auths.clear();
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        auths.put(authData.authToken(), authData);
        if (this.getAuth(authData) == null) {
            throw new DataAccessException("Error: unable to add new authToken to auths");
        }
        return authData;
    }

    public void deleteAuth(AuthData authData) {
        auths.remove(authData.authToken());
    }

    public AuthData getAuth(AuthData authData) {
        return auths.get(authData.authToken());
    }
}
