package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import request.CreateRequest;
import result.CreateResult;
import service.GameService;

import java.util.Map;

public class CreateHandler implements Handler {
    private final GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void handle(@NotNull Context context) {
        try {
            CreateRequest createRequest = new Gson().fromJson(context.body(), CreateRequest.class);
            String requestedAuthToken = context.header("authorization");
            CreateResult createResult = gameService.create(requestedAuthToken, createRequest);
            context.result(new Gson().toJson(createResult));
        }
        catch(Exception e) {
            if (e.getClass() == BadRequestResponse.class) {
                context.status(400);
            }
            else if (e.getClass() == UnauthorizedResponse.class) {
                context.status(401);
            }
            else if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
