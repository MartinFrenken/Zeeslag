package zeeslag.server.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zeeslag.server.WebSocketServerEventListener;
import zeeslag.shared.HitType;
import zeeslag.shared.Ship;

import java.io.IOException;
import java.util.Objects;

class WebSocketServer extends WebSocketAdapter {

    private final WebSocketEventServlet webSocketEventServlet;
    private final WebSocketServerEventListener eventListener;
    @Nullable
    private String token;
    private Session session;


    boolean isSpectator() {
        return isSpectator;
    }


    private boolean isSpectator = false;


    WebSocketServer(WebSocketEventServlet webSocketEventServlet, WebSocketServerEventListener eventListener) {
        this.webSocketEventServlet = webSocketEventServlet;
        this.eventListener = eventListener;
    }


    @Override
    public void onWebSocketConnect(@NotNull Session session) {
        super.onWebSocketConnect(session);
        webSocketEventServlet.getSocketServers().add(this);
        this.session = session;

        System.out.println("Socket Connected: " + session);
    }


    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        var json = new Gson().fromJson(message, JsonObject.class);
        var action = json.get("action");

        if (action != null && action.getAsString().equals("spectator")) {
            isSpectator = true;
            return;
        }

        if (token == null) {
            var jsonToken = json.get("token");
            if (jsonToken != null)
                token = jsonToken.getAsString();
            return;
        }
        if (webSocketEventServlet.getPlayerId(token) != json.get("sender").getAsInt())
            return;
        if (isSpectator)
            return;

        switch (Objects.requireNonNull(action).getAsString()) {
            case "placeShips":
                onPlaceShips(json);
                return;
            case "attack":
                onAttack(json);
                return;
            case "singlePlayer":
                eventListener.onSinglePlayer();
                return;
            case "reset":
                eventListener.onReset();
                return;
            default:
                throw new IllegalStateException("Invalid action: " + action);
        }
    }


    private void onAttack(JsonObject json) {
        var data = json.get("data").getAsJsonObject();
        var userId = json.get("sender").getAsInt();
        var x = data.get("x").getAsInt();
        var y = data.get("y").getAsInt();

        eventListener.onAttack(userId, x, y);
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
        webSocketEventServlet.removeFromAuthMap(Objects.requireNonNull(token));
        webSocketEventServlet.getSocketServers().remove(this);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }


    @Override
    public void onWebSocketError(@NotNull Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }


    void emitStart() {
        try {
            var json = new JsonObject();
            json.addProperty("action", "start");
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void emitAttackResult(int receiver, int x, int y, @NotNull HitType hit) {
        try {
            var json = new JsonObject();
            json.addProperty("action", "attackResult");

            var data = new JsonObject();
            data.addProperty("receiver", receiver);
            data.addProperty("x", x);
            data.addProperty("y", y);
            data.addProperty("hitType", hit.toString());
            json.add("data", data);
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void emitReset() {
        var json = new JsonObject();
        json.addProperty("action", "reset");
        try {
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void emitPlaceShips(int userId, Ship[] ships) {
        var json = new JsonObject();
        json.addProperty("action", "placeShips");
        json.addProperty("sender", userId);
        json.add("data", new Gson().toJsonTree(ships));

        try {
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}