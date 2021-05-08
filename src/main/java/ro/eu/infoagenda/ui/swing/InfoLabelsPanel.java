package ro.eu.infoagenda.ui.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InfoLabelsPanel {
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
    private JLabel newsFeederInfoLabel;

    private boolean newsFeederInfoLabelVisibile = true;

    public InfoLabelsPanel() {
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

    public JLabel getNewsFeederInfoLabel() {
        return newsFeederInfoLabel;
    }

    public void showOrHideNewsFeederInfoLabel() {
        newsFeederInfoLabelVisibile = !newsFeederInfoLabelVisibile;
        if (newsFeederInfoLabelVisibile) {
            newsFeederInfoLabel.setForeground(timeInfoLabel.getForeground());
        }else {
            newsFeederInfoLabel.setForeground(TEXT_COLOR.BLACK.color);
        }
    }

    public void updateLabelTextColor() {
        final Color nextColor = getNextLabelTextColor();

        timeInfoLabel.setForeground(nextColor);
        dateInfoLabel.setForeground(nextColor);
        weatherCurrentOutsideTempInfoLabel.setForeground(nextColor);
        if (newsFeederInfoLabelVisibile) {
            newsFeederInfoLabel.setForeground(nextColor);
        }
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

        newsFeederInfoLabel = new JLabel("");
        newsFeederInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newsFeederInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 50));
        newsFeederInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

        JPanel timeDateWeatherPanel = new JPanel();
        timeDateWeatherPanel.setBackground(Color.BLACK);
        timeDateWeatherPanel.setLayout(new BoxLayout(timeDateWeatherPanel, BoxLayout.Y_AXIS));
        timeDateWeatherPanel.add(timeInfoLabel);
        timeDateWeatherPanel.add(dateInfoLabel);
        timeDateWeatherPanel.add(weatherCurrentOutsideTempInfoLabel);

        labelsPanel = new JPanel();
        labelsPanel.setBackground(Color.BLACK);
        labelsPanel.setLayout(new BorderLayout());
        labelsPanel.add(timeDateWeatherPanel, BorderLayout.CENTER);
        labelsPanel.add(newsFeederInfoLabel, BorderLayout.SOUTH);
        labelsPanel.setBorder(new EmptyBorder(100, 100, 0, 0));
    }
}
