package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.RegisterRequest;
import result.RegisterResult;
import service.UserService;

import java.util.Map;

public class RegisterHandler implements Handler {
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(@NotNull Context context) {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
            RegisterResult registerResult = userService.register(registerRequest);
            context.result(new Gson().toJson(registerResult));
        }
        catch (Exception e) {
            if (e.getClass() == BadRequestResponse.class) {
                context.status(400);
            }
            if (e.getClass() == ForbiddenResponse.class) {
                context.status(403);
            }
            if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
