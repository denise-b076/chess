package service;

import model.AuthData;
import model.GameData;
import requestresult.*;

import java.util.ArrayList;

public class GameService {

    private final dataaccess.MemoryGameDAO gameDAO;
    private final dataaccess.MemoryAuthDao authDao;

    public GameService(dataaccess.MemoryGameDAO gameDAO, dataaccess.MemoryAuthDao authDao) {
        this.gameDAO = gameDAO;
        this.authDao = authDao;
    }

    public void join(AuthData authData, JoinRequest joinRequest) throws RequestException {
        if (authDao.getAuth(authData) == null) {
            throw new RequestException("invalid authData");
        }
        GameData game = gameDAO.getGame(joinRequest.gameID());
        if (game == null) {
            throw new RequestException("game DNE");
        }
        gameDAO.updateGame(joinRequest.playerColor(), authData.username(), game);
    }

    public CreateResult create(AuthData authData, CreateRequest createRequest) throws RequestException {
        if (authDao.getAuth(authData) == null) {
            throw new RequestException("invalid authData");
        }
        return new CreateResult(gameDAO.createGame(createRequest.gameName()));
    }

    public ListResult list(AuthData authData) throws RequestException {
        if (authDao.getAuth(authData) == null) {
            throw new RequestException("invalid authData");
        }
        ArrayList<GameData> games = gameDAO.listGames();
        return new ListResult(games);
    }
}
