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

    private JLabel weatherCurrentOutsideTempInfoLabel;
    private JLabel weatherRealFeelTemperatureInfoLabel;
    private JLabel weatherWindKmPerHInfoLabel;
    private JLabel weatherOverAllLabel;
    private JLabel weatherPressureLabel;

    private JLabel newsFeederInfoLabel;

    private boolean newsFeederInfoLabelVisible = true;

    public InfoLabelsPanel() {
        this.build();
    }

    public JPanel getLabelsPanel() {
        return labelsPanel;
    }

    public JLabel getTimeInfoLabel() {
        return timeInfoLabel;
    }

    public JLabel getWeatherCurrentOutsideTempInfoLabel() {
        return weatherCurrentOutsideTempInfoLabel;
    }

    public JLabel getWeatherPressureLabel() {
        return weatherPressureLabel;
    }

    public JLabel getWeatherRealFeelTemperatureInfoLabel() {
        return weatherRealFeelTemperatureInfoLabel;
    }

    public JLabel getWeatherWindKmPerHInfoLabel() {
        return weatherWindKmPerHInfoLabel;
    }

    public JLabel getWeatherOverAllLabel() {
        return weatherOverAllLabel;
    }

    public JLabel getNewsFeederInfoLabel() {
        return newsFeederInfoLabel;
    }

    public void showOrHideNewsFeederInfoLabel() {
        newsFeederInfoLabelVisible = !newsFeederInfoLabelVisible;
        if (newsFeederInfoLabelVisible) {
            newsFeederInfoLabel.setForeground(timeInfoLabel.getForeground());
        }else {
            newsFeederInfoLabel.setForeground(TEXT_COLOR.BLACK.color);
        }
    }

    public void updateLabelTextColor() {
        final Color nextColor = getNextLabelTextColor();

        timeInfoLabel.setForeground(nextColor);
        weatherCurrentOutsideTempInfoLabel.setForeground(nextColor);
        weatherPressureLabel.setForeground(nextColor);
        weatherRealFeelTemperatureInfoLabel.setForeground(nextColor);
        weatherWindKmPerHInfoLabel.setForeground(nextColor);
        weatherOverAllLabel.setForeground(nextColor);

        if (newsFeederInfoLabelVisible) {
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

        newsFeederInfoLabel = new JLabel("");
        newsFeederInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newsFeederInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 50));
        newsFeederInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

        JPanel timeWeatherPanel = new JPanel();
        timeWeatherPanel.setBackground(Color.BLACK);
        timeWeatherPanel.setLayout(new BoxLayout(timeWeatherPanel, BoxLayout.Y_AXIS));
        timeWeatherPanel.add(timeInfoLabel);
        timeWeatherPanel.add(createWeatherPanel());

        labelsPanel = new JPanel();
        labelsPanel.setBackground(Color.BLACK);
        labelsPanel.setLayout(new BorderLayout());
        labelsPanel.add(timeWeatherPanel, BorderLayout.CENTER);
        labelsPanel.add(newsFeederInfoLabel, BorderLayout.SOUTH);
        labelsPanel.setBorder(new EmptyBorder(10, 100, 0, 0));
    }

    private JPanel createWeatherPanel() {
        weatherRealFeelTemperatureInfoLabel = new JLabel("");
        weatherRealFeelTemperatureInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        weatherRealFeelTemperatureInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 30));
        weatherRealFeelTemperatureInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

        weatherWindKmPerHInfoLabel = new JLabel("");
        weatherWindKmPerHInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        weatherWindKmPerHInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 30));
        weatherWindKmPerHInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

        weatherPressureLabel  = new JLabel("");
        weatherPressureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        weatherPressureLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 30));
        weatherPressureLabel.setForeground(TEXT_COLOR.GREEN.color);

        JPanel weatherPanel = new JPanel();
        weatherPanel.setBackground(Color.BLACK);
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.add(createCurrentTemperaturePanel());
        weatherPanel.add(weatherRealFeelTemperatureInfoLabel);
        weatherPanel.add(weatherWindKmPerHInfoLabel);
        weatherPanel.add(weatherPressureLabel);

        return weatherPanel;
    }

    private JPanel createCurrentTemperaturePanel() {
        weatherCurrentOutsideTempInfoLabel = new JLabel("");
        weatherCurrentOutsideTempInfoLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 50));
        weatherCurrentOutsideTempInfoLabel.setForeground(TEXT_COLOR.GREEN.color);

        weatherOverAllLabel = new JLabel("");
        weatherOverAllLabel.setFont(new Font(timeInfoLabel.getName(), Font.PLAIN, 50));
        weatherOverAllLabel.setForeground(TEXT_COLOR.GREEN.color);
        weatherOverAllLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel currentTemperaturePanel = new JPanel();
        currentTemperaturePanel.setBackground(Color.BLACK);
        currentTemperaturePanel.setLayout(new BoxLayout(currentTemperaturePanel, BoxLayout.X_AXIS));
        currentTemperaturePanel.add(weatherCurrentOutsideTempInfoLabel);
        currentTemperaturePanel.add(weatherOverAllLabel);
        currentTemperaturePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return currentTemperaturePanel;
    }
}
