module com.svalero.shop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    opens controller to javafx.fxml;
    opens com.svalero.shop to javafx.fxml;
    exports com.svalero.shop;
    exports controller;
}