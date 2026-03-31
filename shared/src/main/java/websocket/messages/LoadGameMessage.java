package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {

    GameData gameData;
    String color;

    public LoadGameMessage(GameData gameData, String color) {
        super(ServerMessageType.LOAD_GAME);
        this.gameData = gameData;
        this.color = color;
    }

    public GameData getGameData() {
        return gameData;
    }

    public String getColor() {
        return color;
    }
}
