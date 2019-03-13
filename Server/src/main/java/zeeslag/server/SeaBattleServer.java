package zeeslag.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SeaBattleServer {


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
        connector.setPort(8080);
        return connector;
    }


    private static ServletContextHandler createContextHandler() {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder("ws-events", WebSocketEventServlet.class), "/events/*");
        return contextHandler;
    }

}
