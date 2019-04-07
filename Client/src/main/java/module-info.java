module Client {
    requires gson;
    requires javafx.graphics;
    requires javafx.controls;
    requires slf4j.api;
    requires unirest.java;
    requires org.eclipse.jetty.websocket.api;
    requires org.eclipse.jetty.websocket.client;
    requires annotations;
    requires Shared;
    requires java.sql;
    exports zeeslag.client.gui;
    exports zeeslag.client.game;
}