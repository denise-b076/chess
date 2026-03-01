package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.util.Map;

public class LogoutHandler implements Handler {
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(@NotNull Context context) {
        try {
            String requestedAuthToken = context.header("authorization");
            userService.logout(requestedAuthToken);
        }
        catch (Exception e) {
            if (e.getClass() == DataAccessException.class) {
                context.status(500);
            }
            else if (e.getClass() == UnauthorizedResponse.class) {
                context.status(401);
            }
            context.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
