package zeeslag.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import zeeslag.server.net.LoginServlet;
import zeeslag.server.net.WebSocketEventServlet;

public class ZeeslagServer {

    private static final Zeeslag game = new Zeeslag(new BasicAi());
    private static final WebSocketEventServlet webSocketServlet = new WebSocketEventServlet(new WebSocketServerEventHandler(game));


    static WebSocketEventServlet getWebSocketServlet() {
        return webSocketServlet;
    }


    public static void main(String... args) {
        Server server = new Server();
        server.addConnector(createConnector(server));
        server.setHandler(createContextHandler());

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static ServerConnector createConnector(Server server) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(3000);
        return connector;
    }


    private static ServletContextHandler createContextHandler() {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder("ws-events", webSocketServlet), "/ws");
        contextHandler.addServlet(new ServletHolder(new LoginServlet(webSocketServlet)), "/api/login");
        return contextHandler;
    }

}
