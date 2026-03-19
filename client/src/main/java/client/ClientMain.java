package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client");
        try {
            new Client(8080).run();
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
