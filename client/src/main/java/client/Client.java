package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.GameData;
import model.request.CreateRequest;
import model.request.JoinRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListResult;
import model.result.LoginResult;
import model.result.RegisterResult;
import serverfacade.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client implements NotificationHandler {

    private String visitorName = null;
    private final ServerFacade server;
    private String authToken = null;
    private final String exitString = SET_TEXT_COLOR_YELLOW + "See you later!";
    private final WebSocketFacade ws;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public Client(int port) throws Exception {
        server = new ServerFacade(port);
        ws = new WebSocketFacade(port, this);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_YELLOW + "Welcome to Denise's 240 chess! Type 'help' to get started!");

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

    public void notify(ServerMessage serverMessage) {
        if (serverMessage instanceof NotificationMessage) {
            notification((NotificationMessage) serverMessage);
        }
        else if (serverMessage instanceof ErrorMessage) {
            error((ErrorMessage) serverMessage);
        }
        else {
            loadGame((LoadGameMessage) serverMessage);
        }
    }

    private void notification(NotificationMessage notificationMessage) {
        System.out.println(SET_TEXT_COLOR_RED + notificationMessage.getMessage());
        printPrompt();
    }

    private void error(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_YELLOW + errorMessage.getErrorMessage());
        printPrompt();
    }

    private void loadGame(LoadGameMessage loadGameMessage) {
        printBoard(loadGameMessage.getColor(), loadGameMessage.getGame());
    }

    private void printPrompt() {
        String authorized = authToken != null ? "[" + visitorName + "]" : "[LOGGED_OUT]";
        String line = "------------\n";
        System.out.println(RESET_TEXT_COLOR + "\n" + line + authorized + " >>>" + SET_TEXT_COLOR_GREEN);
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
            return e.getMessage() + "\n";
        }
    }

    private String register(String... params) throws Exception {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = server.registerUser(request);
            visitorName = result.username();
            authToken = result.authToken();
            return String.format("You're signed in as: " + visitorName + "\n");
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    private String login(String... params) throws Exception {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResult result = server.loginUser(request);
            visitorName = result.username();
            authToken = result.authToken();
            return String.format("You're signed in as: " + visitorName + "\n");
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD>");
    }

    private String logout() throws Exception {
        server.logoutUser(authToken);
        visitorName = null;
        authToken = null;
        return "You've signed out\n";
    }

    private String create(String... params) throws Exception {
        if (params.length == 1) {
            CreateRequest request = new CreateRequest(params[0]);
            server.createGame(request, authToken);
            return String.format("New game created: " + request.gameName() + "\n");
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
        if (listString.isEmpty()) {
            return "No games created yet\n";
        }
        return listString.toString();
    }

    private String join(String... params) throws Exception {
        if (params.length == 2) {
            try {
                int requestedID = Integer.parseInt(params[0]);
                if (requestedID > games.size() || requestedID < 1) {
                    throw new Exception("Error: Invalid game ID");
                }
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
                if (requestedID > games.size() || requestedID < 1) {
                    throw new Exception("Error: Invalid game ID");
                }
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
        String borderColors = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE;
        String columns = "    a  b  c  d  e  f  g  h    ";
        String letterLabel;
        if (color.equals("WHITE")) {
            letterLabel = borderColors + columns + RESET_BG_COLOR + "\n";
            board.append(letterLabel);
            for (int i = 8; i > 0; i--) {
                StringBuilder currRow = rowBuilder(requestedGame,color,borderColors,i);
                board.append(currRow);
            }
        }
        else {
            letterLabel = borderColors + new StringBuilder(columns).reverse() + RESET_BG_COLOR + "\n";
            board.append(letterLabel);
            for (int i = 1; i <= 8; i++) {
                StringBuilder currRow = rowBuilder(requestedGame,color,borderColors,i);
                board.append(currRow);
            }
        }
        board.append(letterLabel);
        return board.toString();
    }

    private StringBuilder rowBuilder(GameData requestedGame, String color, String borderColors, int row) {
        String numberLabel = borderColors + " " + row + " ";
        StringBuilder currRow = new StringBuilder(numberLabel);
        if (color.equals("WHITE")) {
            for (int j = 1; j <= 8; j++) {
                String square = squareBuilder(requestedGame, row, j);
                currRow.append(square);
            }
        }
        else {
            for (int j = 8; j > 0; j--) {
                String square = squareBuilder(requestedGame, row, j);
                currRow.append(square);
            }
        }
        String rowCap = numberLabel + RESET_BG_COLOR + "\n";
        currRow.append(rowCap);
        return currRow;
    }

    private String squareBuilder(GameData requestedGame, int row, int col) {
        ChessPiece currPiece = requestedGame.game().getBoard().getPiece(new ChessPosition(row, col));
        String pieceBackground = setSquareBackground(row, col);
        String pieceStyling = setPieceStyling(currPiece);
        return pieceBackground + pieceStyling;
    }

    private String setSquareBackground(int row, int col) {
        if (row % 2 != 0) {
            return col % 2 == 0 ? SET_BG_COLOR_AZURE : SET_BG_COLOR_STRATOS;
        }
        else {
            return col % 2 != 0 ? SET_BG_COLOR_AZURE : SET_BG_COLOR_STRATOS;
        }
    }

    private String setPieceStyling(ChessPiece currPiece) {
        if (currPiece == null) {
            return "   ";
        }
        else {
            String pieceColor = currPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_GOLD : SET_TEXT_COLOR_ELECTRIC_GREEN;
            String pieceType;
            switch (currPiece.getPieceType()) {
                case KNIGHT -> pieceType = " N ";
                case ROOK -> pieceType = " R ";
                case BISHOP -> pieceType = " B ";
                case QUEEN -> pieceType = " Q ";
                case KING -> pieceType = " K ";
                default -> pieceType = " P ";
            }
            return pieceColor + pieceType;
        }
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
