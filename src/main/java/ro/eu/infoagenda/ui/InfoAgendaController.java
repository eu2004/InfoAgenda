package ro.eu.infoagenda.ui;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
        PaintService paintService = new PaintService();
        paintService.start();
    }

    private class PaintService extends AnimationTimer {
        private static final long ONE_SECOND_NANOS = 1_000_000_000L;
        private long nextSecond = 0L;

        @Override
        public void start() {
            nextSecond = 0L;
            super.start();
        }
        @Override
        public void handle(long nanoseconds) {
            if (nanoseconds < nextSecond) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }

            nextSecond = nanoseconds + ONE_SECOND_NANOS;

            try {
                weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
            } catch (IOException e) {
                e.printStackTrace();
            }

            dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());

            timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());
        }
    }
}
