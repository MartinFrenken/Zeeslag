package zeeslag.client.game;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import zeeslag.shared.HitType;

import java.net.URI;

public class ZeeslagSpectatorClient extends WebSocketAdapter {

    private final ZeeslagWebSocketEventListener eventListener;
    private final WebSocketClient client = new WebSocketClient();
    private Session session;


    ZeeslagSpectatorClient(String url, ZeeslagWebSocketEventListener eventListener) {
        this.eventListener = eventListener;

        try {
            client.start();
            session = client.connect(this, URI.create(url)).get();
            var json = new JsonObject();
            json.addProperty("action", "spectator");
            session.getRemote().sendString(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("Duplicates")
    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        var json = new Gson().fromJson(message, JsonObject.class);
        var action = json.get("action").getAsString();

        switch (action) {
            case "attackResult":
                onAttackResult(json);
                break;
            case "ready":
                eventListener.onReady();
                break;
            case "start":
                eventListener.onStart();
                break;
            case "reset":
                eventListener.onReset();
                break;
            default:
                throw new IllegalStateException("Invalid action: " + action);
        }
    }


    @SuppressWarnings("Duplicates")
    private void onAttackResult(JsonObject json) {
        var data = json.get("data").getAsJsonObject();
        var to = data.get("receiver").getAsInt();
        var x = data.get("x").getAsInt();
        var y = data.get("y").getAsInt();
        var hitType = new Gson().fromJson(data.get("hitType"), HitType.class);
        eventListener.onAttackResult(to, x, y, hitType);
    }

}
