package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.*;
import org.jetbrains.annotations.NotNull;
import request.LoginRequest;
import result.LoginResult;
import service.UserService;

import java.util.Map;

public class LoginHandler implements Handler {
    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(@NotNull Context context) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);
            context.result(new Gson().toJson(loginResult));
        }
        catch(Exception e) {
            if (e.getClass() == BadRequestResponse.class) {
                context.status(400);
            }
            if (e.getClass() == UnauthorizedResponse.class) {
                context.status(401);
            }
            if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
