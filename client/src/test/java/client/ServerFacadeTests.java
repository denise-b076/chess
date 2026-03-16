package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import request.CreateRequest;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        server.clearDatabase();
    }


    @Test
    public void registerTest() throws Exception {
        var authData = facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginTest() throws Exception {
        facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        var authData = facade.loginUser(new LoginRequest("player1", "password"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void logoutTest() throws Exception {
        var authData = facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        assertDoesNotThrow(() -> facade.logoutUser(authData.authToken()));
    }

    @Test
    void createGameTest() throws Exception {
        var authData = facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        var gameID = facade.createGame(new CreateRequest("newGame"), authData.authToken());
        assertEquals(1, gameID.gameID());
    }

    @Test
    void registerAlreadyExists() throws Exception {
        facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(Exception.class, () -> facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com")));
    }

    @Test
    void loginNonExistent() {
        assertThrows(Exception.class, () -> facade.loginUser(new LoginRequest("player1", "password")));
    }

    @Test
    void logoutNonExistent() {
        assertThrows(Exception.class, () -> facade.logoutUser("bad"));
    }

    @Test
    void createGameUnauthorized() {
        assertThrows(Exception.class, () -> facade.createGame(new CreateRequest("newGame"), "bad"));
    }

}
