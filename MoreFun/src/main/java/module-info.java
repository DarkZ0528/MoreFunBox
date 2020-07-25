module club.digitallove {
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires com.jfoenix;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.desktop;
    requires fastjson;
    requires org.jsoup;
    requires jetty.util;

    opens club.digitallove.controller to javafx.graphics, javafx.controls, javafx.fxml;
    opens club.digitallove to javafx.graphics, javafx.controls, javafx.fxml;
}
