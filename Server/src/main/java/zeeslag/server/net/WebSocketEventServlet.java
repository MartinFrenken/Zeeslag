package zeeslag.server.net;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import zeeslag.server.WebSocketServerEventListener;
import zeeslag.shared.HitType;
import zeeslag.shared.net.UserAuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketEventServlet extends WebSocketServlet implements LoginListener {

    private final Map<String, Integer> authMap = new HashMap<>();
    private final Set<WebSocketServer> socketServers = new HashSet<>();
    private final WebSocketServerEventListener eventListener;


    public WebSocketEventServlet(WebSocketServerEventListener eventListener) {
        this.eventListener = eventListener;
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator((req, resp) -> new WebSocketServer(this, eventListener));
    }


    @Override
    public int getNewUserId() {
        if (authMap.size() > 2) throw new IllegalStateException("Authmap larger than 2");
        eventListener.onPlayerJoin(authMap.size());
        return authMap.size();
    }


    @Override
    public void addToAuthMap(@NotNull UserAuthData authData) {
        authMap.put(authData.token, authData.id);
    }


    void removeFromAuthMap(@NotNull String token) {
        eventListener.onPlayerLeave(authMap.get(token));
        authMap.remove(token);
    }


    int getPlayerId(@NotNull String token) {
        return authMap.get(token);
    }


    public void emitStart() {
        for (WebSocketServer socketServer : socketServers)
            socketServer.emitStart();
    }


    Set<WebSocketServer> getSocketServers() {
        return socketServers;
    }


    public void emitAttackResult(int receiver, int x, int y, HitType hit) {
        for (WebSocketServer socketServer : socketServers)
            socketServer.emitAttackResult(receiver, x, y, hit);
    }


    public void emitReset() {
        for (WebSocketServer socketServer : socketServers)
            socketServer.emitReset();
    }

}
