module com.svalero.shop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.svalero.shop to javafx.fxml;
    exports com.svalero.shop;
}