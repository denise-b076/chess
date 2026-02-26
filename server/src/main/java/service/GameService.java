package service;

import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import result.*;

import java.util.ArrayList;

public class GameService {

    private final dataaccess.GameDAO gameDAO;
    private final dataaccess.AuthDAO authDAO;

    public GameService(dataaccess.GameDAO gameDAO, dataaccess.AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void join(String authToken, JoinRequest joinRequest) throws UnauthorizedResponse, DataAccessException, BadRequestResponse {
        AuthData requestedAuthData = authDAO.getAuth(authToken);
        if (requestedAuthData == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        GameData game = gameDAO.getGame(joinRequest.gameID());
        if (game == null) {
            throw new BadRequestResponse("Error: bad request");
        }
        gameDAO.updateGame(joinRequest.playerColor(), requestedAuthData.username(), game);
    }

    public CreateResult create(String authToken, CreateRequest createRequest) throws UnauthorizedResponse, BadRequestResponse, DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        if (createRequest.gameName() == null) {
            throw new BadRequestResponse("Error: bad request");
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
