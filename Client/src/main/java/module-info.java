module Client {
    requires gson;
    requires java.sql;
    requires java.logging;
    requires javafx.graphics;
    requires javafx.controls;
    requires slf4j.api;
    requires httpclient;
    requires unirest.java;
    requires org.apache.httpcomponents.httpcore;
    requires org.eclipse.jetty.websocket.api;
    requires org.eclipse.jetty.websocket.client;
    exports zeeslag.client.gui;
    exports zeeslag.client.game;
}