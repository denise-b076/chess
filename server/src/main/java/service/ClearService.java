package service;

import dataaccess.DataAccessException;

public class ClearService {

    private final dataaccess.GameDAO gameDAO;
    private final dataaccess.AuthDAO authDao;
    private final dataaccess.UserDAO userDAO;

    public ClearService(dataaccess.GameDAO gameDAO, dataaccess.AuthDAO authDao, dataaccess.UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDao = authDao;
        this.userDAO = userDAO;
    }

    public void clear() throws DataAccessException {
        gameDAO.clearGames();
        authDao.clearAuths();
        userDAO.clearUsers();
    }
}
