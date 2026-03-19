package client;

import org.junit.jupiter.api.*;
import model.request.CreateRequest;
import model.request.JoinRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.CreateResult;
import model.result.RegisterResult;
import server.Server;
import serverfacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private RegisterResult authData;
    private CreateResult createResult;

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
    void clearAndReset() throws Exception {
        server.clearDatabase();
        authData = facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com"));
        createResult = facade.createGame(new CreateRequest("newGame"), authData.authToken());
        facade.joinGame(new JoinRequest("WHITE", createResult.gameID()), authData.authToken());
    }


    @Test
    public void registerTest() throws Exception {
        var authData = facade.registerUser(new RegisterRequest("player2", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginTest() throws Exception {
        var authData = facade.loginUser(new LoginRequest("player1", "password"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void logoutTest() {
        assertDoesNotThrow(() -> facade.logoutUser(authData.authToken()));
    }

    @Test
    void createGameTest() throws Exception {
        var gameID = facade.createGame(new CreateRequest("newGame"), authData.authToken());
        assertEquals(2, gameID.gameID());
    }

    @Test
    void joinGameTest() {
        assertDoesNotThrow(() -> facade.joinGame(new JoinRequest("BLACK", createResult.gameID()), authData.authToken()));
    }

    @Test
    void listGamesTest() throws Exception {
        assertEquals(1, facade.listGames(authData.authToken()).games().size());
    }

    @Test
    void registerAlreadyExists() {
        assertThrows(Exception.class, () -> facade.registerUser(new RegisterRequest("player1", "password", "p1@email.com")));
    }

    @Test
    void loginNonExistent() {
        assertThrows(Exception.class, () -> facade.loginUser(new LoginRequest("player2", "password")));
    }

    @Test
    void logoutNonExistent() {
        assertThrows(Exception.class, () -> facade.logoutUser("bad"));
    }

    @Test
    void createGameUnauthorized() {
        assertThrows(Exception.class, () -> facade.createGame(new CreateRequest("newGame"), "bad"));
    }

    @Test
    void joinGameColorTaken() throws Exception {
        var authData2 = facade.registerUser(new RegisterRequest("thief", "thief", "thief@thief.com"));
        assertThrows(Exception.class, () -> facade.joinGame(new JoinRequest("WHITE", createResult.gameID()), authData2.authToken()));
    }

    @Test
    void listGamesUnauthorized() {
        assertThrows(Exception.class, () -> facade.listGames("bad"));
    }

}
