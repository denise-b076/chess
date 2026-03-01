package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    static final UserDAO userDAO = new MemoryUserDAO();
    static final GameDAO gameDAO = new MemoryGameDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService clearService = new ClearService(gameDAO, authDAO, userDAO);
    static final GameService gameService = new GameService(gameDAO, authDAO);
    static final UserService userService = new UserService(authDAO, userDAO);
    static final String username = "username";
    static final String password = "password";
    static final String email = "email";

    @BeforeEach
    void clearAndRegister() throws DataAccessException {
        clearService.clear();
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        userService.register(registerRequest);
    }

    @Test
    void loginSuccess() throws DataAccessException {
        LoginResult actualLogin = userService.login(new LoginRequest(username, password));
        assertTrue(actualLogin.username().equals(username) && actualLogin.authToken() != null);
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        assertDoesNotThrow(() -> userService.logout(login.authToken()));
    }

    @Test
    void registerSameUser() {
        RegisterRequest identicalRequest = new RegisterRequest(username, password, email);
        assertThrows(ForbiddenResponse.class, () -> userService.register(identicalRequest));
    }

    @Test
    void loginWrongPassword() {
        LoginRequest wrongPassword = new LoginRequest(username, "batman");
        assertThrows(UnauthorizedResponse.class, () -> userService.login(wrongPassword));
    }

    @Test
    void logoutInvalidAuth() throws DataAccessException {
        userService.login(new LoginRequest(username, password));
        assertThrows(UnauthorizedResponse.class, () -> userService.logout("batman"));
    }
}
