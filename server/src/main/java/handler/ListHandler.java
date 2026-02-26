package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import requestresult.ListResult;
import service.GameService;

import java.util.Map;

public class ListHandler implements Handler {

    private final GameService gameService;

    public ListHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void handle(@NotNull Context context) {
        try {
            String requestedAuthToken = context.header("authorization");
            ListResult listResult = gameService.list(requestedAuthToken);
            context.result(new Gson().toJson(listResult));
        }
        catch(Exception e) {
            if (e.getClass() == UnauthorizedResponse.class) {
                context.status(401);
            }
            else if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
