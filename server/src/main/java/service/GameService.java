package service;

import dataaccess.DataAccessException;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.GameData;
import requestresult.*;

import java.util.ArrayList;

public class GameService {

    private final dataaccess.GameDAO gameDAO;
    private final dataaccess.AuthDAO authDAO;

    public GameService(dataaccess.GameDAO gameDAO, dataaccess.AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void join(AuthData authData, JoinRequest joinRequest) throws RequestException, DataAccessException {
        if (authDAO.getAuth(authData.authToken()) == null) {
            throw new RequestException("invalid authData");
        }
        GameData game = gameDAO.getGame(joinRequest.gameID());
        if (game == null) {
            throw new RequestException("game DNE");
        }
        gameDAO.updateGame(joinRequest.playerColor(), authData.username(), game);
    }

    public CreateResult create(AuthData authData, CreateRequest createRequest) throws RequestException, DataAccessException {
        if (authDAO.getAuth(authData.authToken()) == null) {
            throw new RequestException("invalid authData");
        }
        return new CreateResult(gameDAO.createGame(createRequest.gameName()));
    }

    public ListResult list(String authToken) throws UnauthorizedResponse, DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        ArrayList<GameData> games = gameDAO.listGames();
        if (games == null) {
            throw new DataAccessException("Error: could not retrieve games");
        }
        return new ListResult(games);
    }
}
