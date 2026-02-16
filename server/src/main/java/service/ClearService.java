package service;

public class ClearService {

    private final dataaccess.MemoryGameDAO gameDAO;
    private final dataaccess.MemoryAuthDao authDao;
    private final dataaccess.MemoryUserDAO userDAO;

    public ClearService(dataaccess.MemoryGameDAO gameDAO, dataaccess.MemoryAuthDao authDao, dataaccess.MemoryUserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDao = authDao;
        this.userDAO = userDAO;
    }

    public void clear() {
        gameDAO.clearGames();
        authDao.clearAuths();
        userDAO.clearUsers();
    }
}
