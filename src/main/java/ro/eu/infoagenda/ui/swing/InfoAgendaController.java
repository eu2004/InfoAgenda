package ro.eu.infoagenda.ui.swing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.eu.infoagenda.service.DateInfoService;
import ro.eu.infoagenda.service.TimeInfoService;
import ro.eu.infoagenda.service.WeatherService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

class InfoAgendaController {
    private static final Logger logger = LogManager.getLogger(ro.eu.infoagenda.ui.InfoAgendaController.class);

    private final TimeInfoService timeInfoService = new TimeInfoService();
    private final DateInfoService dateInfoService = new DateInfoService();
    private final WeatherService weatherService = new WeatherService();

    public void show() {
        final JFrame frame = createFrame();

        // center the JLabel
        LabelsPanel labelsPanel = new LabelsPanel();

        // add JLabel to JFrame
        frame.getContentPane().add(labelsPanel.getLabelsPanel(), BorderLayout.CENTER);

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelsPanel.updateLabelTextColor();
            }
        });

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
        refreshDateTimeLabels(timeInfoLabel, dateInfoLabel);

        final Timer timer = new Timer(1000, event -> {
            refreshDateTimeLabels(timeInfoLabel, dateInfoLabel);
            loadWeatherInfo(weatherCurrentOutsideTempInfoLabel);
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(1000);
        timer.start();
    }

    private void refreshDateTimeLabels(JLabel timeInfoLabel, JLabel dateInfoLabel) {
        timeInfoLabel.setText(timeInfoService.getCurrentTime().getInfo().getContent());
        dateInfoLabel.setText(dateInfoService.getCurrentDate().getInfo().getContent());
    }

    private void loadWeatherInfo(JLabel weatherCurrentOutsideTempInfoLabel) {
        try {
            weatherCurrentOutsideTempInfoLabel.setText(weatherService.getOutsideCurrentTemperature().getInfo().getContent() + " Outside");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private static class LabelsPanel {
        enum TEXT_COLOR {
            GREEN(Color.GREEN),
            RED(Color.RED),
            BLUE(Color.BLUE),
            GRAY(Color.GRAY),
            DARK_GRAY(Color.DARK_GRAY),
            BLACK(Color.BLACK);

            private final Color color;

            TEXT_COLOR(Color color) {
                this.color = color;
            }

        }

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

        public void updateLabelTextColor() {
            final Color nextColor = getNextLabelTextColor();

            timeInfoLabel.setForeground(nextColor);
            dateInfoLabel.setForeground(nextColor);
            weatherCurrentOutsideTempInfoLabel.setForeground(nextColor);
        }

        private Color getNextLabelTextColor() {
            TEXT_COLOR[] textColors = TEXT_COLOR.values();
            int currentTextColor = 0;
            for (; currentTextColor < textColors.length; currentTextColor++) {
                if (textColors[currentTextColor].color.equals(timeInfoLabel.getForeground())) {
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

        private void build() {
            timeInfoLabel = new JLabel("");
            timeInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            timeInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 150));
            timeInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

            dateInfoLabel = new JLabel("");
            dateInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dateInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 60));
            dateInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

            weatherCurrentOutsideTempInfoLabel = new JLabel("");
            weatherCurrentOutsideTempInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            weatherCurrentOutsideTempInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 60));
            weatherCurrentOutsideTempInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

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
