package service;

import model.AuthData;
import model.UserData;
import requestresult.*;

import java.util.UUID;

public class UserService {

    private final dataaccess.MemoryUserDAO userDAO;
    private final dataaccess.MemoryAuthDao authDao;

    public UserService(dataaccess.MemoryAuthDao authDao, dataaccess.MemoryUserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDao = authDao;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws RequestException {
        if (userDAO.getUser(registerRequest.username()) != null) {
            throw new RequestException("username already exists");
        }
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        String authToken = generateToken();
        userDAO.createUser(new UserData(username, password, email));
        authDao.createAuth(new AuthData(username, authToken));
        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws RequestException {
        if (userDAO.getUser(loginRequest.username()) == null) {
            throw new RequestException("username does not exist");
        }
        String username = loginRequest.username();
        String authToken = generateToken();
        authDao.createAuth(new AuthData(username, authToken));
        return new LoginResult(username, authToken);
    }

    public void logout(AuthData authData) throws RequestException {
        if (authDao.getAuth(authData) == null) {
            throw new RequestException("invalid authData");
        }
        authDao.deleteAuth(authData);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
