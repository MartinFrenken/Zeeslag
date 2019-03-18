package zeeslag.server.net;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zeeslag.shared.net.UserAuthData;

import java.util.HashMap;
import java.util.Map;

public class WebSocketEventServlet extends WebSocketServlet implements LoginListener {

    private static final Map<String, Integer> authMap = new HashMap<>();


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator((req, resp) -> new WebSocketEventHandler(this));
    }


    @Override
    public int getNewUserId() {
        return authMap.size();
    }


    @Override
    public void addToAuthMap(@NotNull UserAuthData authData) {
        authMap.put(authData.token, authData.id);
    }


    void removeFromAuthMap(@Nullable String token) {
        authMap.remove(token);
    }


    int getPlayerId(@NotNull String token) {
        return authMap.get(token);
    }

}
