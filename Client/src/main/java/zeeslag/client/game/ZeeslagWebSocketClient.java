package zeeslag.client.game;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.Future;

public class ZeeslagWebSocketClient extends WebSocketAdapter {

    public ZeeslagWebSocketClient() {
        URI uri = URI.create("ws://localhost:3000/events/");

        WebSocketClient client = new WebSocketClient();
        try {
            try {
                client.start();
                Future<Session> fut = client.connect(this, uri);
                Session session = fut.get();
                session.getRemote().sendString("Hello");
                session.close();
            } finally {
                client.stop();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
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
        System.out.println("Received TEXT message: " + message);
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
