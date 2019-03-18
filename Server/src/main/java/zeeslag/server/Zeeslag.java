package zeeslag.server;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.jetbrains.annotations.NotNull;
import zeeslag.server.net.WebSocketEventServlet;
import zeeslag.server.net.util.LoginListener;
import zeeslag.shared.net.UserAuthData;

public class Zeeslag implements LoginListener {

    private final WebSocketEventServlet webSocketServlet;
    @NotNull
    public GameState gameState = GameState.PREPARING;
    private int playerCount = 0;


    public Zeeslag(@NotNull WebSocketEventServlet webSocketServlet) {
        this.webSocketServlet = webSocketServlet;
    }


    @Override
    public int getNewUserId() {
        return playerCount++;
    }


    @Override
    public void addToAuthTable(@NotNull UserAuthData authData) {
        
    }


    @NotNull
    public WebSocketServlet getWebSocketServlet() {
        return webSocketServlet;
    }

}
