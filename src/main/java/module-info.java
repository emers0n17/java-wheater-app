module com.wheater.tempo_aplicativo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;

    opens com.wheater.tempo_aplicativo to javafx.fxml;
    exports com.wheater.tempo_aplicativo;
}
