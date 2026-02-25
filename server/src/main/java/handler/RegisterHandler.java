package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import requestresult.RequestException;
import service.UserService;

public class RegisterHandler implements Handler {
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context) throws DataAccessException, RequestException {
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        context.result(new Gson().toJson(registerResult));
    }
}
