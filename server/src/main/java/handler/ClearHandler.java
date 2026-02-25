package handler;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;

public class ClearHandler implements Handler{
    private final ClearService clearService;
    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void handle(Context context) throws DataAccessException {
        clearService.clear();
        context.status(200);
    }
}
