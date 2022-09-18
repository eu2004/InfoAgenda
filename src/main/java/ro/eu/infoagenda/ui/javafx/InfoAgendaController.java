package ro.eu.infoagenda.ui.javafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.WeatherDetails;
import ro.eu.infoagenda.service.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoAgendaController implements Initializable {
    private static final Logger logger = LogManager.getLogger(InfoAgendaController.class);
    private static final int MAX_CURRENT_NEWS_LENGTH = 100;
    private static final int CURRENT_NEWS_DISPLAY_SPEED = 5;

    private final TimeInfoService timeInfoService = new TimeInfoService();
    private final DateInfoService dateInfoService = new DateInfoService();
    private final WeatherService weatherService = new WeatherService();
    private final NewsFeederService newsFeederService = new NewsFeederService();

    private int currentNewsStartPos = 0;

    public BorderPane infoAgendaPane;
    public Label timeInfoLabel;
    public Label dateInfoLabel;
    public Label weatherCurrentOutsideTempInfoLabel;
    public Label newsFeederInfoLabel;
    private boolean newsFeederInfoLabelVisible = true;

    enum TEXT_COLOR {
        GREEN(Color.GREEN),
        RED(Color.RED),
        BLUE(Color.BLUE),
        GRAY(Color.GRAY),
        DARK_GRAY(Color.DARKGRAY),
        BLACK(Color.BLACK);

        private final Color color;

        TEXT_COLOR(Color color) {
            this.color = color;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateInfoLabel.setTextFill(TEXT_COLOR.GREEN.color);
        weatherCurrentOutsideTempInfoLabel.setTextFill(TEXT_COLOR.GREEN.color);
        timeInfoLabel.setTextFill(TEXT_COLOR.GREEN.color);
        newsFeederInfoLabel.setTextFill(TEXT_COLOR.GREEN.color);

        addEventFilter();

        dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());
        timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshInfo()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void addEventFilter() {
        infoAgendaPane.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                mouseEvent -> {
                    final Color nextColor = getNextLabelTextColor();
                    timeInfoLabel.setTextFill(nextColor);
                    dateInfoLabel.setTextFill(nextColor);
                    weatherCurrentOutsideTempInfoLabel.setTextFill(nextColor);
                    if (newsFeederInfoLabelVisible) {
                        newsFeederInfoLabel.setTextFill(nextColor);
                    }
                });

        newsFeederInfoLabel.addEventFilter(MouseEvent.MOUSE_PRESSED,
                mouseEvent -> showOrHideNewsFeederInfoLabel());
    }

    private void showOrHideNewsFeederInfoLabel() {
        newsFeederInfoLabelVisible = !newsFeederInfoLabelVisible;
        if (newsFeederInfoLabelVisible) {
            newsFeederInfoLabel.setTextFill(timeInfoLabel.getTextFill());
        } else {
            newsFeederInfoLabel.setTextFill(TEXT_COLOR.BLACK.color);
        }
    }

    private Color getNextLabelTextColor() {
        TEXT_COLOR[] textColors = TEXT_COLOR.values();
        int currentTextColor = 0;
        for (; currentTextColor < textColors.length; currentTextColor++) {
            if (textColors[currentTextColor].color.equals(timeInfoLabel.getTextFill())) {
                if (currentTextColor < textColors.length - 1) {
                    currentTextColor++;
                } else {
                    currentTextColor = 0;
                }
                break;
            }
        }
        return textColors[currentTextColor].color;
    }

    private void refreshInfo() {
        dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());
        timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());

        try {
            Info<WeatherDetails> details = weatherService.getLocalWeatherInfo();
            //TODO update it weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
        } catch (IOException e) {
            logger.error(e);
        }

        refreshNewsFeederInfo(newsFeederInfoLabel);
    }

    private synchronized void refreshNewsFeederInfo(Label newsFeederInfoLabel) {
        try {
            java.util.List<String> news = newsFeederService.getLocalNews().getInfo().getContent();
            if (news == null || news.isEmpty()) {
                currentNewsStartPos = 0;
                newsFeederInfoLabel.setText("");
            } else {
                newsFeederInfoLabel.setText(loadNextNews(news));
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private String loadNextNews(java.util.List<String> news) {
        final StringBuilder currentNews = new StringBuilder();
        final int currentNewsStopPos = currentNewsStartPos + MAX_CURRENT_NEWS_LENGTH;
        int currentNewsCharacterStart = 0;

        for (int i = 0; i < news.size() && currentNewsCharacterStart < currentNewsStopPos; i++) {
            String newsArticle = news.get(i);
            for (int j = 0; j < newsArticle.length() && currentNewsCharacterStart < currentNewsStopPos; j++) {
                if (currentNewsCharacterStart >= currentNewsStartPos) {
                    currentNews.append(newsArticle.charAt(j));
                }
                currentNewsCharacterStart++;
            }
        }

        currentNewsStartPos += CURRENT_NEWS_DISPLAY_SPEED;
        int maxNewsLength = news.stream().mapToInt(String::length).sum();
        if (currentNewsStartPos >= maxNewsLength) {
            currentNewsStartPos = 0;
        }
        return currentNews.toString();
    }
}
