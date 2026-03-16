package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameID;
import model.GameName;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;

    public ServerFacade(int port) {
        this.url = "http://localhost:" + port;
    }

    public AuthData registerUser(UserData userData) throws Exception {
        var request = buildRequest("POST", "/user", userData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData loginUser(UserData userData) throws Exception {
        var request = buildRequest("POST", "/session", userData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logoutUser(String token) throws Exception {
        var request = buildRequest("DELETE", "/session", null, token);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public GameID createGame(GameName gameName, String token) throws Exception {
        var request = buildRequest("POST", "/game", gameName, token);
        var response = sendRequest(request);
        return handleResponse(response, GameID.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String token) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (token != null) {
            request.setHeader("authorization", token);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        }
        else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception(new Gson().fromJson(body, String.class));
            }
            throw new Exception("other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
