package zeeslag.server.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.jetbrains.annotations.Nullable;

public class WebSocketEventHandler extends WebSocketAdapter {

    private final WebSocketEventServlet webSocketEventServlet;
    @Nullable
    private String token;


    public WebSocketEventHandler(WebSocketEventServlet webSocketEventServlet) {
        this.webSocketEventServlet = webSocketEventServlet;
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
        if (token == null)
            token = json.get("token").getAsString();
        if (webSocketEventServlet.getPlayerId(token) != json.get("sender").getAsInt())
            return;
        System.out.println("Received TEXT message: " + message);
    }


    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        webSocketEventServlet.removeFromAuthMap(token);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }


    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

}