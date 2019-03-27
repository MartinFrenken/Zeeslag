package zeeslag.server.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.jetbrains.annotations.Nullable;
import zeeslag.server.WebSocketServerEventListener;
import zeeslag.shared.net.Ship;

import java.io.IOException;

class WebSocketServer extends WebSocketAdapter {

    private final WebSocketEventServlet webSocketEventServlet;
    private final WebSocketServerEventListener eventListener;
    @Nullable
    private String token;
    private Session session;


    public WebSocketServer(WebSocketEventServlet webSocketEventServlet, WebSocketServerEventListener eventListener) {
        this.webSocketEventServlet = webSocketEventServlet;
        this.eventListener = eventListener;
    }


    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        webSocketEventServlet.getSocketServers().add(this);
        this.session = session;

        System.out.println("Socket Connected: " + session);
    }


    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        var json = new Gson().fromJson(message, JsonObject.class);
        if (token == null)
            token = json.get("token").getAsString();
        if (webSocketEventServlet.getPlayerId(token) != json.get("sender").getAsInt())
            return;

        var action = json.get("action").getAsString();

        switch (action) {
            case "placeShips":
                onPlaceShips(json);
                break;
            default:
                throw new IllegalStateException("Invalid action: " + action);
        }
    }


    private void onPlaceShips(JsonObject json) {
        var data = json.get("data").getAsJsonArray();
        var userId = json.get("sender").getAsInt();
        var ships = new Gson().fromJson(data, Ship[].class);

        if (ships == null) return;

        eventListener.onPlaceShips(userId, ships);
    }


    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        webSocketEventServlet.getSocketServers().remove(this);
        webSocketEventServlet.removeFromAuthMap(token);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }


    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }


    public void emitStart() {
        try {
            var json = new JsonObject();
            json.addProperty("action", "start");
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}