package server;

import dataaccess.*;
import handler.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;


public class Server {

    private final ClearService clearService;
    private final GameService gameService;
    private final UserService userService;

    private final Javalin javalin;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        this.clearService = new ClearService(gameDAO, authDAO, userDAO);
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(gameDAO, authDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        createHandlers(javalin);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void createHandlers(Javalin javalinServer) {
        javalinServer.delete("/db", new ClearHandler(clearService))
                .post("/user", new RegisterHandler(userService))
                .post("/session", new LoginHandler(userService))
                .delete("/session", new LogoutHandler(userService))
                .get("/game", new ListHandler(gameService));
    }


    public void stop() {
        javalin.stop();
    }
}
