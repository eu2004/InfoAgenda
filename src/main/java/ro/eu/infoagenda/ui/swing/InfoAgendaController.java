package ro.eu.infoagenda.ui.swing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.eu.infoagenda.service.DateInfoService;
import ro.eu.infoagenda.service.NewsFeederService;
import ro.eu.infoagenda.service.TimeInfoService;
import ro.eu.infoagenda.service.WeatherService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

class InfoAgendaController {
    private static final Logger logger = LogManager.getLogger(ro.eu.infoagenda.ui.InfoAgendaController.class);
    private static final int MAX_CURRENT_NEWS_LENGTH = 100;
    private static final int CURRENT_NEWS_DISPLAY_SPEED = 5;

    private final TimeInfoService timeInfoService = new TimeInfoService();
    private final DateInfoService dateInfoService = new DateInfoService();
    private final WeatherService weatherService = new WeatherService();
    private final NewsFeederService newsFeederService = new NewsFeederService();

    private int currentNewsStartPos = 0;

    public void show() {
        final JFrame frame = createFrame();

        // center the JLabel
        InfoLabelsPanel infoLabelsPanel = new InfoLabelsPanel();

        // add JLabel to JFrame
        frame.getContentPane().add(infoLabelsPanel.getLabelsPanel(), BorderLayout.CENTER);

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infoLabelsPanel.updateLabelTextColor();
            }
        });
        infoLabelsPanel.getNewsFeederInfoLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infoLabelsPanel.showOrHideNewsFeederInfoLabel();
            }
        });

        // display it
        frame.pack();
        frame.setVisible(true);

        refreshUI(infoLabelsPanel.getTimeInfoLabel(),
                infoLabelsPanel.getDateInfoLabel(),
                infoLabelsPanel.getWeatherCurrentOutsideTempInfoLabel(),
                infoLabelsPanel.getNewsFeederInfoLabel());
    }

    private JFrame createFrame() {
        final JFrame frame = new JFrame("Info agenda");

        // set frame site
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(Color.BLACK);
        frame.getContentPane().setLayout(new BorderLayout());
        return frame;
    }

    private void refreshUI(JLabel timeInfoLabel, JLabel dateInfoLabel, JLabel weatherCurrentOutsideTempInfoLabel, JLabel newsfeederInfoLabel) {
        refreshDateTimeInfo(timeInfoLabel, dateInfoLabel);

        final Timer timer = new Timer(1000, event -> {
            refreshDateTimeInfo(timeInfoLabel, dateInfoLabel);
            refreshWeatherInfo(weatherCurrentOutsideTempInfoLabel);
            refreshNewsFeederInfo(newsfeederInfoLabel);
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(1000);
        timer.start();
    }

    private void refreshDateTimeInfo(JLabel timeInfoLabel, JLabel dateInfoLabel) {
        timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());
        dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());
    }

    private void refreshWeatherInfo(JLabel weatherCurrentOutsideTempInfoLabel) {
        try {
            weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private synchronized void refreshNewsFeederInfo(JLabel newsFeederInfoLabel) {
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
                if (currentNewsCharacterStart >= currentNewsStartPos && currentNewsCharacterStart < currentNewsStopPos) {
                    currentNews.append(newsArticle.charAt(j));
                }
                currentNewsCharacterStart++;
            }
        }

        currentNewsStartPos += CURRENT_NEWS_DISPLAY_SPEED;
        int maxNewsLength = news.stream().mapToInt(item -> item.length()).sum();
        if (currentNewsStartPos >= maxNewsLength) {
            currentNewsStartPos = 0;
        }
        return currentNews.toString();
    }
}
