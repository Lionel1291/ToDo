module ch.bztf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;

    opens ch.bztf to javafx.fxml;
    exports ch.bztf;
}