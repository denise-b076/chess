package service;

import dataaccess.DataAccessException;
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

    public ListResult list(AuthData authData) throws RequestException, DataAccessException {
        if (authDAO.getAuth(authData.authToken()) == null) {
            throw new RequestException("invalid authData");
        }
        ArrayList<GameData> games = gameDAO.listGames();
        return new ListResult(games);
    }
}
