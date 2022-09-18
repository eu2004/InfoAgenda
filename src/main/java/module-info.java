module infoagenda {
    requires org.jsoup;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.desktop;

    opens ro.eu.infoagenda.ui;
    opens ro.eu.infoagenda.ui.javafx;
}