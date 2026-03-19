package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    private final ServerFacade server;
    private String authToken = null;
    private final String exitString = SET_TEXT_COLOR_YELLOW + "See you later!";
    private final HashMap<Integer, GameData> games = new HashMap<>();

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
        String authorized = authToken != null ? "[" + visitorName + "]" : "[LOGGED_OUT]";
        System.out.println("\n" + RESET_TEXT_COLOR + authorized + " >>>" + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (authToken == null) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> exitString;
                    default -> help();
                };
            }
            else {
                return switch(cmd) {
                    case "logout" -> logout();
                    case "create" -> create(params);
                    case "list" -> list();
                    case "join" -> join(params);
                    case "observe" -> observe(params);
                    case "quit" -> exitString;
                    default -> help();
                };
            }
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

    private String login(String... params) throws Exception {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResult result = server.loginUser(request);
            visitorName = result.username();
            authToken = result.authToken();
            return String.format("You're signed in as: " + visitorName);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD>");
    }

    private String logout() throws Exception {
        server.logoutUser(authToken);
        visitorName = null;
        authToken = null;
        return "you've signed out";
    }

    private String create(String... params) throws Exception {
        if (params.length == 1) {
            CreateRequest request = new CreateRequest(params[0]);
            server.createGame(request, authToken);
            return String.format("New game created: " + request.gameName());
        }
        throw new Exception("Expected: <NAME>");
    }

    private String list() throws Exception {
        games.clear();
        ListResult result = server.listGames(authToken);
        StringBuilder listString = new StringBuilder();
        ArrayList<GameData> listOfGames = result.games();
        for (int i = 1; i <= listOfGames.size(); i++) {
            GameData currGame = listOfGames.get(i - 1);
            String currWhite = currGame.whiteUsername() == null ? "<NONE>" : currGame.whiteUsername();
            String currBlack = currGame.blackUsername() == null ? "<NONE>" : currGame.blackUsername();
            listString.append(String.format(i + " - name: " + currGame.gameName() + ", white: " + currWhite + ", black: " + currBlack + "\n"));
            games.put(i, listOfGames.get(i - 1));
        }
        return listString.toString();
    }

    private String join(String... params) throws Exception {
        if (params.length == 2) {
            try {
                int requestedID = Integer.parseInt(params[0]);
                GameData requestedGame = games.get(requestedID);
                JoinRequest request = new JoinRequest(params[1].toUpperCase(), requestedGame.gameID());
                server.joinGame(request, authToken);
                return printBoard(params[1].toUpperCase(), requestedGame);
            }
            catch (NumberFormatException e) {
                throw new Exception("Expected: <ID> [WHITE|BLACK]");
            }
        }
        throw new Exception("Expected: <ID> [WHITE|BLACK]");
    }

    private String observe(String... params) throws Exception {
        if (params.length == 1) {
            try {
                int requestedID = Integer.parseInt(params[0]);
                GameData requestedGame = games.get(requestedID);
                return printBoard("WHITE", requestedGame);
            }
            catch (NumberFormatException e) {
                throw new Exception("Expected: <ID>");
            }
        }
        throw new Exception("Expected: <ID>");
    }

    private String printBoard(String color, GameData requestedGame) {
        StringBuilder board = new StringBuilder();
        String outsideColors = SET_BG_COLOR_BLUE + SET_TEXT_COLOR_WHITE;
        String letterLabel = color.equals("WHITE") ? outsideColors + "    a  b  c  d  e  f  g  h    " + SET_BG_COLOR_BLACK + "\n" : outsideColors + "    h  g  f  e  d  c  b  a    " + SET_BG_COLOR_BLACK + "\n";
        board.append(letterLabel);
        if (color.equals("WHITE")) {
            for (int i = 8; i > 0; i--) {
                String numberLabel = outsideColors + " " + i + " ";
                StringBuilder currRow = new StringBuilder(numberLabel);
                for (int j = 1; j <= 8; j++) {
                    ChessPiece currPiece = requestedGame.game().getBoard().getPiece(new ChessPosition(i, j));
                    String pieceBackground = j % 2 !=0 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
                    String pieceColor;
                    String pieceType;
                    if (currPiece == null) {
                        pieceType = "   ";
                        pieceColor = "";
                    }
                    else {
                        pieceColor = currPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_RED;
                        switch (currPiece.getPieceType()) {
                            case KNIGHT -> pieceType = " N ";
                            case ROOK -> pieceType = " R ";
                            case BISHOP -> pieceType = " B ";
                            case QUEEN -> pieceType = " Q ";
                            case KING -> pieceType = " K ";
                            default -> pieceType = " P ";
                        }
                    }
                    String square = pieceBackground + pieceColor + pieceType;
                    currRow.append(square);
                }
                currRow.append(numberLabel);
                currRow.append(SET_BG_COLOR_BLACK);
                currRow.append("\n");
                board.append(currRow);
            }
        }
        else {
            for (int i = 1; i <= 8; i++) {
                String numberLabel = outsideColors + " " + i + " ";
                StringBuilder currRow = new StringBuilder(numberLabel);
                for (int j = 1; j <= 8; j++) {
                    ChessPiece currPiece = requestedGame.game().getBoard().getPiece(new ChessPosition(i, j));
                    String pieceBackground = j % 2 != 0 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
                    String pieceColor;
                    String pieceType;
                    if (currPiece == null) {
                        pieceType = "   ";
                        pieceColor = "";
                    }
                    else {
                        pieceColor = currPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_RED;
                        switch (currPiece.getPieceType()) {
                            case KNIGHT -> pieceType = " N ";
                            case ROOK -> pieceType = " R ";
                            case BISHOP -> pieceType = " B ";
                            case QUEEN -> pieceType = " Q ";
                            case KING -> pieceType = " K ";
                            default -> pieceType = " P ";
                        }
                    }
                    String square = pieceBackground + pieceColor + pieceType;
                    currRow.append(square);
                }
                currRow.append(numberLabel);
                currRow.append(SET_BG_COLOR_BLACK);
                currRow.append("\n");
                board.append(currRow);
            }
        }
        board.append(letterLabel);
        return board.toString();
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
               join <ID> [WHITE|BLACK] - a game
               observe <ID> - a game
               logout - when you are done
               quit - playing chess
               help - with possible commands
               """;
    }

}
