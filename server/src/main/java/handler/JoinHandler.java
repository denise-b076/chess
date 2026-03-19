package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.*;
import org.jetbrains.annotations.NotNull;
import model.request.JoinRequest;
import service.GameService;

import java.util.Map;

public class JoinHandler implements Handler {

    private final GameService gameService;

    public JoinHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void handle(@NotNull Context context) {
        try {
            JoinRequest joinRequest = new Gson().fromJson(context.body(), JoinRequest.class);
            String requestedAuthToken = context.header("authorization");
            gameService.join(requestedAuthToken, joinRequest);
        }
        catch (Exception e) {
            if (e.getClass() == UnauthorizedResponse.class) {
                context.status(401);
            }
            else if (e.getClass() == BadRequestResponse.class) {
                context.status(400);
            }
            else if (e.getClass() == ForbiddenResponse.class) {
                context.status(403);
            }
            else if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
