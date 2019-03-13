module Client {
    requires gson;
    requires java.sql;
    requires java.logging;
    requires javafx.graphics;
    requires javafx.controls;
    requires slf4j.api;
    requires httpclient;
    exports seabattlegui;
}