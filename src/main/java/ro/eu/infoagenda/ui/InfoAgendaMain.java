package ro.eu.infoagenda.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InfoAgendaMain extends Application {
    private static final Logger logger = LogManager.getLogger(InfoAgendaMain.class);
    private static final String VERSION = "1.1";
    static {
        logger.info(String.format("Info agenda %s", VERSION));
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("infoagenda.fxml"));
        primaryStage.setTitle("Info agenda");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
