package client;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

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
        var authData = facade.registerUser(new UserData("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginTest() throws Exception {
        facade.registerUser(new UserData("player1", "password", "p1@email.com"));
        var authData = facade.loginUser(new UserData("player1", "password", null));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerAlreadyExists() throws Exception {
        facade.registerUser(new UserData("player1", "password", "p1@email.com"));
        assertThrows(Exception.class, () -> facade.registerUser(new UserData("player1", "password", "p1@email.com")));
    }

    @Test
    void loginNonExistent() {
        assertThrows(Exception.class, () -> facade.loginUser(new UserData("not", "here", null)));
    }

}
