package client;

import request.RegisterRequest;
import result.RegisterResult;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    private final ServerFacade server;
    private String authToken = null;
    private final String exitString = SET_TEXT_COLOR_YELLOW + "See you later!";

    public Client(int port) {
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Welcome to Denise's 240 chess! Type 'help' to get started!");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(exitString)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            }
            catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.println("\n" + RESET_TEXT_COLOR + ">>>" + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "quit" -> exitString;
                default -> help();
            };
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String register(String... params) throws Exception {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = server.registerUser(request);
            visitorName = result.username();
            authToken = result.authToken();
            return String.format("You're signed in as: " + visitorName);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    private String help() {
        if (authToken == null) {
            return """
                   register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                   login <USERNAME> <PASSWORD> - to play chess
                   quit - playing chess
                   help - with possible commands
                   """;
        }
        return """
               create <NAME> - a game
               list - games
               join <ID> - a game
               observe <ID> - a game
               logout - when you are done
               quit - playing chess
               help - with possible commands
               """;
    }

}
