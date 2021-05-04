package ro.eu.infoagenda.ui.swing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.eu.infoagenda.service.DateInfoService;
import ro.eu.infoagenda.service.TimeInfoService;
import ro.eu.infoagenda.service.WeatherService;
import ro.eu.infoagenda.ui.InfoAgendaController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class InfoAgendaMain {
    private static final Logger logger = LogManager.getLogger(InfoAgendaController.class);

    private final TimeInfoService timeInfoService = new TimeInfoService();
    private final DateInfoService dateInfoService = new DateInfoService();
    private final WeatherService weatherService = new WeatherService();

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new InfoAgendaMain().show());
    }

    public void show() {
        final JFrame frame = createFrame();

        // center the JLabel
        LabelsPanel labelsPanel = new LabelsPanel();

        // add JLabel to JFrame
        frame.getContentPane().add(labelsPanel.getLabelsPanel(), BorderLayout.CENTER);

        // display it
        frame.pack();
        frame.setVisible(true);

        refreshUI(labelsPanel.getTimeInfoLabel(), labelsPanel.getDateInfoLabel(), labelsPanel.getWeatherCurrentOutsideTempInfoLabel());
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

    private void refreshUI(JLabel timeInfoLabel, JLabel dateInfoLabel, JLabel weatherCurrentOutsideTempInfoLabel) {
        final Timer timer = new Timer(1000, event -> {
            if (weatherCurrentOutsideTempInfoLabel.getText().isEmpty()) {
                new Thread(()-> loadWeatherInfo(weatherCurrentOutsideTempInfoLabel)).start();
            }else {
                loadWeatherInfo(weatherCurrentOutsideTempInfoLabel);
            }

            dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());

            timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    private void loadWeatherInfo(JLabel weatherCurrentOutsideTempInfoLabel) {
        try {
            weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private static class LabelsPanel {
        private JPanel labelsPanel;
        private JLabel timeInfoLabel;
        private JLabel dateInfoLabel;
        private JLabel weatherCurrentOutsideTempInfoLabel;

        public LabelsPanel() {
            this.build();
        }

        public JPanel getLabelsPanel() {
            return labelsPanel;
        }

        public JLabel getTimeInfoLabel() {
            return timeInfoLabel;
        }

        public JLabel getDateInfoLabel() {
            return dateInfoLabel;
        }

        public JLabel getWeatherCurrentOutsideTempInfoLabel() {
            return weatherCurrentOutsideTempInfoLabel;
        }

        private void build() {
            timeInfoLabel = new JLabel("");
            timeInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            timeInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 100));
            timeInfoLabel.setForeground(Color.GREEN);

            dateInfoLabel = new JLabel("");
            dateInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dateInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 40));
            dateInfoLabel.setForeground(Color.GREEN);

            weatherCurrentOutsideTempInfoLabel = new JLabel("");
            weatherCurrentOutsideTempInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            weatherCurrentOutsideTempInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 40));
            weatherCurrentOutsideTempInfoLabel.setForeground(Color.GREEN);

            labelsPanel = new JPanel();
            labelsPanel.setBackground(Color.BLACK);
            labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
            labelsPanel.add(timeInfoLabel);
            labelsPanel.add(dateInfoLabel);
            labelsPanel.add(weatherCurrentOutsideTempInfoLabel);
            labelsPanel.setBorder(new EmptyBorder(100, 100, 0, 0));
        }
    }
}
