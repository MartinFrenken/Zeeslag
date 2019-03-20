package zeeslag.client.game;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.boats.Ship;

import java.io.IOException;
import java.net.URI;

public class ZeeslagWebSocketClient extends WebSocketAdapter {

    private final String token;
    private final int playerId;
    private final WebSocketClient client = new WebSocketClient();
    private final ZeeslagWebSocketEventListener eventListener;
    private Session session;


    public ZeeslagWebSocketClient(@NotNull String url, @NotNull String token, int playerId, @NotNull ZeeslagWebSocketEventListener eventListener) {
        this.token = token;
        this.playerId = playerId;
        this.eventListener = eventListener;

        try {
            client.start();
            session = client.connect(this, URI.create(url)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            try {
//                client.start();
//                Future<Session> fut = client.connect(this, URI.create(url));
//                Session session = fut.get();
//                session.getRemote().sendString("Hello");
//            } finally {
//                session.close();
//                client.stop();
//            }
//        } catch (Throwable t) {
//            t.printStackTrace(System.err);
//        }
    }


    public void disconnect() {
        try {
            session.close();
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JsonObject createBaseActionJson(String action) {
        var json = new JsonObject();
        json.addProperty("action", action);
        json.addProperty("sender", playerId);
        json.addProperty("token", token);
        return json;
    }


    void emitPlaceShips(@NotNull Ship[] ships) {
        try {
            var json = createBaseActionJson("placeShips");
            json.add("data", new Gson().toJsonTree(ships));
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void emitAttack(int x, int y) {
        try {
            var json = createBaseActionJson("attack");
            var data = new JsonObject();
            data.addProperty("x", x);
            data.addProperty("y", y);
            json.add("data", data);
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void emitStart() {
        try {
            var json = createBaseActionJson("start");
            session.getRemote().sendString(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Socket Connected: " + session);
    }


    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        var json = new Gson().fromJson(message, JsonObject.class);
        var action = json.get("action").getAsString();

        if (json.get("sender").getAsInt() == playerId)
            return;

        switch (action) {
            case "attack":
                onAttack(json);
                break;
            case "ready":
                onReady(json);
                break;
            case "start":
                onStart();
                break;
            case "end":
                onEnd();
                break;
            default:
                throw new IllegalStateException("Invalid action: " + action);
        }
    }


    private void onAttack(@NotNull JsonObject json) {
        var position = json.get("data").getAsJsonObject();
        var x = position.get("x").getAsInt();
        var y = position.get("y").getAsInt();
        eventListener.onAttack(json.get("sender").getAsInt(), x, y);
    }


    private void onReady(@NotNull JsonObject json) {
        var userId = json.get("sender").getAsInt();
        eventListener.onReady(userId);
    }


    private void onStart() {
        eventListener.onStart();
    }


    private void onEnd() {
        eventListener.onEnd();
    }


    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }


    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

}
