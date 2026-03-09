package service;

import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;

import java.util.UUID;

public class UserService {

    private final dataaccess.UserDAO userDAO;
    private final dataaccess.AuthDAO authDao;

    public UserService(dataaccess.AuthDAO authDao, dataaccess.UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDao = authDao;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, BadRequestResponse, ForbiddenResponse {
        if (registerRequest == null ||
                registerRequest.email() == null ||
                registerRequest.username() == null ||
                registerRequest.password() == null) {
            throw new BadRequestResponse("Error: bad request");
        }
        if (userDAO.getUser(registerRequest.username()) != null) {
            throw new ForbiddenResponse("Error: already taken");
        }
        String username = registerRequest.username();
        String password = passwordHash(registerRequest.password());
        String email = registerRequest.email();
        String authToken = generateToken();
        userDAO.createUser(new UserData(username, password, email));
        authDao.createAuth(new AuthData(authToken, username));
        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedResponse, DataAccessException {
        if (loginRequest == null ||
                loginRequest.username() == null ||
                loginRequest.password() == null) {
            throw new BadRequestResponse("Error: bad request");
        }
        if (userDAO.getUser(loginRequest.username()) == null ||
                !BCrypt.checkpw(loginRequest.password(), userDAO.getUser(loginRequest.username()).password())) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        String username = loginRequest.username();
        String authToken = generateToken();
        authDao.createAuth(new AuthData(authToken, username));
        return new LoginResult(username, authToken);
    }

    public void logout(String authToken) throws UnauthorizedResponse, DataAccessException {
        AuthData requestedAuthData = authDao.getAuth(authToken);
        if (requestedAuthData == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        authDao.deleteAuth(requestedAuthData);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private String passwordHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
