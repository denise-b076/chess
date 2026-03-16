package server;

import com.google.gson.Gson;
import request.CreateRequest;
import request.JoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;

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

    public RegisterResult registerUser(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult loginUser(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logoutUser(String token) throws Exception {
        var request = buildRequest("DELETE", "/session", null, token);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public CreateResult createGame(CreateRequest createRequest, String token) throws Exception {
        var request = buildRequest("POST", "/game", createRequest, token);
        var response = sendRequest(request);
        return handleResponse(response, CreateResult.class);
    }

    public void joinGame(JoinRequest joinRequest, String token) throws Exception {
        var request = buildRequest("PUT", "/game", joinRequest, token);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListResult listGames(String token) throws Exception {
        var request = buildRequest("GET", "/game", null, token);
        var response = sendRequest(request);
        return handleResponse(response, ListResult.class);
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
