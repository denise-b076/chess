package client;

import server.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    private final ServerFacade server;
    private String authToken = null;

    public Client(int port) {
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Welcome to Denise's 240 chess! Type 'help' to get started!");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
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
        return switch (input) {
            case "quit" -> SET_TEXT_COLOR_YELLOW + "See you later!";
            default -> "You just input: " + input;
        };
    }

}
