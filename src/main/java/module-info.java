module com.shop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.mariadb.jdbc;
    opens model to javafx.base;
    opens controller to javafx.fxml;
    exports controller;
}