package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {

    GameData game;
    String color;

    public LoadGameMessage(GameData gameData, String color) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gameData;
        this.color = color;
    }

    public GameData getGame() {
        return game;
    }

    public String getColor() {
        return color;
    }
}
