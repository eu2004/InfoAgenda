package ro.eu.infoagenda.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import ro.eu.infoagenda.service.DateInfoService;
import ro.eu.infoagenda.service.TimeInfoService;
import ro.eu.infoagenda.service.WeatherService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoAgendaController implements Initializable {
    private final TimeInfoService timeInfoService = new TimeInfoService();
    private final DateInfoService dateInfoService = new DateInfoService();
    private final WeatherService weatherService = new WeatherService();
    public Label timeInfoLabel;
    public Label dateInfoLabel;
    public Label weatherCurrentOutsideTempInfoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshInfo()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void refreshInfo() {
        try {
            weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
        } catch (IOException e) {
            e.printStackTrace();
        }

        dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());

        timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());
    }
}
