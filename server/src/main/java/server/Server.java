package server;

import dataaccess.*;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.sql.DatabaseManager;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import handler.*;
import io.javalin.*;
import server.websocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;


public class Server {

    private final ClearService clearService;
    private final GameService gameService;
    private final UserService userService;
    private final WebSocketHandler webSocketHandler;

    private final Javalin javalin;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        try {
            configureDatabase();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        } catch (Exception ex){
            System.out.print("SQL usage failed, using memory DAOs");
        }
            this.clearService = new ClearService(gameDAO, authDAO, userDAO);
            this.userService = new UserService(authDAO, userDAO);
            this.gameService = new GameService(gameDAO, authDAO);
            this.webSocketHandler = new WebSocketHandler(gameDAO, authDAO);

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
                .get("/game", new ListHandler(gameService))
                .post("/game", new CreateHandler(gameService))
                .put("/game", new JoinHandler(gameService))
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
    }


    public void stop() {
        javalin.stop();
    }

    public static final String[] CREATE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS user (
            user varchar(256) NOT NULL,
            password varchar(256) NOT NULL,
            email varchar(256) NOT NULL,
            PRIMARY KEY (user)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
            game_id int NOT NULL AUTO_INCREMENT,
            game_name varchar(256) NOT NULL,
            white_username varchar(256),
            black_username varchar(256),
            game_json TEXT DEFAULT NULL,
            PRIMARY KEY(game_id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
            token varchar(256) NOT NULL,
            user varchar(256) NOT NULL,
            PRIMARY KEY (token)
            );
            """
    };

    public void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : CREATE_STATEMENTS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
