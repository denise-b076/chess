package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;

import java.util.Map;

public class ClearHandler implements Handler{
    private final ClearService clearService;
    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void handle(Context context) {
        try {
            clearService.clear();
            context.status(200);
        }
        catch (DataAccessException e) {
            context.status(500);
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
