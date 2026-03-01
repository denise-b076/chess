package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.JoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    static ClearService clearService;
    static GameService gameService;
    static UserService userService;
    static final String username = "username";
    static final String password = "password";
    static final String email = "email";

    @BeforeEach
    void resetAndRegister() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        clearService = new ClearService(gameDAO, authDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(gameDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        userService.register(registerRequest);
    }

    @Test
    void registerSuccess() throws DataAccessException {
        String newUser = "newUser";
        String newPass = "newPass";
        String newEmail = "newEmail";
        RegisterRequest registerRequest = new RegisterRequest(newUser, newPass, newEmail);
        RegisterResult actualRegister = userService.register(registerRequest);
        assertTrue(actualRegister.username().equals(newUser) && actualRegister.authToken() != null);
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
    void createSuccess() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        CreateResult actual = gameService.create(login.authToken(), new CreateRequest("gameName"));
        CreateResult expected = new CreateResult(1);
        assertEquals(expected, actual);
    }

    @Test
    void joinSuccess() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        CreateResult game = gameService.create(login.authToken(), new CreateRequest("gameName"));
        assertDoesNotThrow(() -> gameService.join(login.authToken(), new JoinRequest("WHITE", game.gameID())));
    }

    @Test
    void listSuccess() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        gameService.create(login.authToken(), new CreateRequest("gameName"));
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        ArrayList<GameData> expectedList = new ArrayList<>();
        expectedList.add(gameData);
        ListResult actualList = gameService.list(login.authToken());
        assertEquals(actualList.games(), expectedList);
    }

    @Test
    void clearSuccess() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        gameService.create(login.authToken(), new CreateRequest("gameName"));
        assertDoesNotThrow(clearService::clear);
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
    void logoutInvalidAuth() {
        assertThrows(UnauthorizedResponse.class, () -> userService.logout("batman"));
    }

    @Test
    void createInvalidAuth() {
        assertThrows(UnauthorizedResponse.class, () -> gameService.create("batman", new CreateRequest("invalidAuth")));
    }

    @Test
    void joinAlreadyTaken() throws DataAccessException {
        LoginResult login = userService.login(new LoginRequest(username, password));
        CreateResult game = gameService.create(login.authToken(), new CreateRequest("gameName"));
        gameService.join(login.authToken(), new JoinRequest("WHITE", game.gameID()));
        assertThrows(ForbiddenResponse.class, () -> gameService.join(login.authToken(), new JoinRequest("WHITE", game.gameID())));
    }

    @Test
    void listInvalidAuth() {
        assertThrows(UnauthorizedResponse.class, () -> gameService.list("batman"));
    }
}
