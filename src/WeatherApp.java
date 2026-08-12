import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import java.net.URL;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main graphical user interface for the Weather Information App.
 *
 * The application allows users to:
 *
 * - Search for weather information by city
 * - View current weather conditions
 * - View temperature and feels-like temperature
 * - View humidity
 * - View wind speed
 * - View atmospheric pressure
 * - View visibility
 * - View weather icons
 * - View a short-term forecast
 * - Convert temperature units
 * - Convert wind-speed units
 * - View recent searches
 * - Clear search history
 * - Automatically change the interface based on time of day
 *
 * SwingWorker is used for API requests so that the GUI
 * remains responsive while network operations are running.
 */
public class WeatherApp extends JFrame {

    // =========================================================
    // GUI COMPONENTS
    // =========================================================

    private JPanel mainPanel;

    private JTextFieldWrapper locationInput;

    private JButton searchButton;

    private JComboBox<String> temperatureUnitBox;

    private JComboBox<String> windUnitBox;

    private JLabel cityLabel;

    private JLabel temperatureLabel;

    private JLabel feelsLikeLabel;

    private JLabel conditionLabel;

    private JLabel humidityLabel;

    private JLabel windLabel;

    private JLabel pressureLabel;

    private JLabel visibilityLabel;

    private JLabel weatherIconLabel;

    private JLabel statusLabel;

    private JLabel timePeriodLabel;

    private JPanel forecastPanel;

    private JList<String> historyList;

    private DefaultListModel<String> historyListModel;

    private JButton clearHistoryButton;

    // =========================================================
    // APPLICATION DATA
    // =========================================================

    private final WeatherAPI weatherAPI;

    private final WeatherHistory weatherHistory;

    private WeatherData currentWeather;

    private List<ForecastData> currentForecast;

    private Timer backgroundTimer;

    /**
     * Creates the Weather Information App.
     */
    public WeatherApp() {

        weatherAPI =
                new WeatherAPI();

        weatherHistory =
                new WeatherHistory();

        setTitle(
                "Weather Information App"
        );

        setSize(
                1200,
                800
        );

        setMinimumSize(
                new Dimension(
                        1000,
                        700
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createGUI();
    }

    // =========================================================
    // CREATE GUI
    // =========================================================

    /**
     * Builds the complete graphical user interface.
     */
    private void createGUI() {

        mainPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new javax.swing.BoxLayout(
                        headerPanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        headerPanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        "Weather Information App",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        timePeriodLabel =
                new JLabel(
                        DynamicBackground
                                .getTimePeriod(),
                        SwingConstants.CENTER
                );

        timePeriodLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        timePeriodLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        headerPanel.add(
                titleLabel
        );

        headerPanel.add(
                timePeriodLabel
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CURRENT WEATHER
        // -----------------------------------------------------

        JPanel currentWeatherPanel =
                createCurrentWeatherPanel();

        // -----------------------------------------------------
        // FORECAST
        // -----------------------------------------------------

        forecastPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                10,
                                10
                        )
                );

        forecastPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Short-Term Forecast"
                )
        );

        for (int i = 0; i < 4; i++) {

            forecastPanel.add(
                    createEmptyForecastCard()
            );
        }

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        centerPanel.add(
                currentWeatherPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                forecastPanel,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // HISTORY
        // -----------------------------------------------------

        JPanel historyPanel =
                createHistoryPanel();

        historyPanel.setPreferredSize(
                new Dimension(
                        260,
                        400
                )
        );

        JPanel centerAndHistoryPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        centerAndHistoryPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        centerAndHistoryPanel.add(
                historyPanel,
                BorderLayout.EAST
        );

        mainPanel.add(
                centerAndHistoryPanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BOTTOM SECTION
        // -----------------------------------------------------

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        // -----------------------------------------------------
        // SEARCH
        // -----------------------------------------------------

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout()
                );

        JLabel locationLabel =
                new JLabel(
                        "Location:"
                );

        locationInput =
                new JTextFieldWrapper(
                        20
                );

        searchButton =
                new JButton(
                        "Search"
                );

        searchPanel.add(
                locationLabel
        );

        searchPanel.add(
                locationInput.getField()
        );

        searchPanel.add(
                searchButton
        );

        bottomPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // UNIT CONTROLS
        // -----------------------------------------------------

        JPanel unitPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        unitPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Display Units"
                )
        );

        temperatureUnitBox =
                new JComboBox<>(
                        new String[]{
                                "Celsius (°C)",
                                "Fahrenheit (°F)"
                        }
                );

        windUnitBox =
                new JComboBox<>(
                        new String[]{
                                "m/s",
                                "km/h",
                                "mph"
                        }
                );

        unitPanel.add(
                new JLabel(
                        "Temperature:"
                )
        );

        unitPanel.add(
                temperatureUnitBox
        );

        unitPanel.add(
                new JLabel(
                        "Wind Speed:"
                )
        );

        unitPanel.add(
                windUnitBox
        );

        bottomPanel.add(
                unitPanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        statusLabel =
                new JLabel(
                        "Ready",
                        SwingConstants.CENTER
                );

        bottomPanel.add(
                statusLabel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // EVENT HANDLERS
        // -----------------------------------------------------

        searchButton.addActionListener(
                event -> searchWeather()
        );

        locationInput.getField()
                .addActionListener(
                        event -> searchWeather()
                );

        temperatureUnitBox
                .addActionListener(
                        event -> refreshUnits()
                );

        windUnitBox
                .addActionListener(
                        event -> refreshUnits()
                );

        // -----------------------------------------------------
        // ADD MAIN PANEL
        // -----------------------------------------------------

        setContentPane(
                mainPanel
        );

        applyDynamicBackground();

        startBackgroundTimer();
    }

    // =========================================================
    // CURRENT WEATHER PANEL
    // =========================================================

    /**
     * Creates the current weather display panel.
     *
     * @return current weather panel
     */
    private JPanel createCurrentWeatherPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new javax.swing.BoxLayout(
                        panel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Current Weather"
                )
        );

        cityLabel =
                createCenteredLabel(
                        "Enter a location",
                        24,
                        Font.BOLD
                );

        weatherIconLabel =
                createCenteredLabel(
                        "",
                        14,
                        Font.PLAIN
                );

        temperatureLabel =
                createCenteredLabel(
                        "Temperature: --",
                        30,
                        Font.BOLD
                );

        feelsLikeLabel =
                createCenteredLabel(
                        "Feels Like: --",
                        14,
                        Font.PLAIN
                );

        conditionLabel =
                createCenteredLabel(
                        "Condition: --",
                        14,
                        Font.PLAIN
                );

        humidityLabel =
                createCenteredLabel(
                        "Humidity: --",
                        14,
                        Font.PLAIN
                );

        windLabel =
                createCenteredLabel(
                        "Wind Speed: --",
                        14,
                        Font.PLAIN
                );

        pressureLabel =
                createCenteredLabel(
                        "Pressure: --",
                        14,
                        Font.PLAIN
                );

        visibilityLabel =
                createCenteredLabel(
                        "Visibility: --",
                        14,
                        Font.PLAIN
                );

        panel.add(cityLabel);
        panel.add(weatherIconLabel);
        panel.add(temperatureLabel);
        panel.add(feelsLikeLabel);
        panel.add(conditionLabel);
        panel.add(humidityLabel);
        panel.add(windLabel);
        panel.add(pressureLabel);
        panel.add(visibilityLabel);

        return panel;
    }

    /**
     * Creates a centered JLabel.
     *
     * @param text label text
     * @param size font size
     * @param style font style
     * @return configured label
     */
    private JLabel createCenteredLabel(
            String text,
            int size,
            int style) {

        JLabel label =
                new JLabel(
                        text,
                        SwingConstants.CENTER
                );

        label.setFont(
                new Font(
                        "Arial",
                        style,
                        size
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // HISTORY PANEL
    // =========================================================

    /**
     * Creates the recent-search history panel.
     *
     * @return history panel
     */
    private JPanel createHistoryPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Recent Weather Searches"
                )
        );

        historyListModel =
                new DefaultListModel<>();

        historyList =
                new JList<>(
                        historyListModel
                );

        historyList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        historyList.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        historyList
                );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        clearHistoryButton =
                new JButton(
                        "Clear History"
                );

        clearHistoryButton.addActionListener(
                event -> clearHistory()
        );

        panel.add(
                clearHistoryButton,
                BorderLayout.SOUTH
        );

        historyList.addListSelectionListener(
                event -> {

                    if (event.getValueIsAdjusting()) {
                        return;
                    }

                    int selectedIndex =
                            historyList
                                    .getSelectedIndex();

                    if (selectedIndex < 0) {
                        return;
                    }

                    List<WeatherHistory.HistoryEntry>
                            entries =
                            weatherHistory
                                    .getHistory();

                    if (selectedIndex >=
                            entries.size()) {

                        return;
                    }

                    WeatherHistory.HistoryEntry entry =
                            entries.get(
                                    selectedIndex
                            );

                    locationInput
                            .getField()
                            .setText(
                                    entry.getCity()
                            );
                }
        );

        return panel;
    }

// =========================================================
// SEARCH
// =========================================================

/**
 * Performs a weather search.
 *
 * The API request is executed through SwingWorker
 * so the graphical interface remains responsive.
 */
private void searchWeather() {

    String city =
            locationInput
                    .getField()
                    .getText()
                    .trim();

    // -----------------------------------------------------
    // INPUT VALIDATION
    // -----------------------------------------------------

    if (city.isEmpty()) {

        showWarning(
                "Please enter a city or location."
        );

        locationInput
                .getField()
                .requestFocus();

        return;
    }

    if (city.length() < 2) {

        showWarning(
                "Please enter a valid location name."
        );

        locationInput
                .getField()
                .requestFocus();

        return;
    }

    if (city.length() > 100) {

        showWarning(
                "The location name is too long."
        );

        locationInput
                .getField()
                .requestFocus();

        return;
    }

    setSearchingState(true);

    statusLabel.setText(
            "Connecting to weather service..."
    );

    SwingWorker<WeatherResult, Void> worker =
            new SwingWorker<>() {

        @Override
        protected WeatherResult doInBackground()
                throws Exception {

            WeatherData weather =
                    weatherAPI.getWeather(city);

            statusLabel.setText(
                    "Retrieving forecast..."
            );

            List<ForecastData> forecast =
                    weatherAPI.getForecast(city);

            return new WeatherResult(
                    weather,
                    forecast
            );
        }

        @Override
        protected void done() {

            try {

                WeatherResult result = get();

                currentWeather =
                        result.currentWeather;

                currentForecast =
                        result.forecast;

                /*
                 * Record only successful searches.
                 */
                weatherHistory.addEntry(
                        currentWeather
                );

                updateCurrentWeather();

                updateForecast();

                updateHistoryList();

                statusLabel.setText(
                        "Weather information updated successfully."
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                showError(
                        "The weather request was interrupted. "
                        + "Please try again."
                );

                statusLabel.setText(
                        "Weather request interrupted."
                );

                e.printStackTrace();

            } catch (java.util.concurrent.ExecutionException e) {

                Throwable cause = e.getCause();

                if (cause instanceof WeatherException) {

                    WeatherException weatherException =
                            (WeatherException) cause;

                    showError(
                            weatherException.getMessage()
                    );

                    statusLabel.setText(
                            "Unable to retrieve weather information."
                    );

                } else {

                    showError(
                            "An unexpected error occurred. "
                            + "Please try again."
                    );

                    statusLabel.setText(
                            "Unexpected application error."
                    );

                    e.printStackTrace();
                }

            } catch (Exception e) {

                showError(
                        "An unexpected error occurred. "
                        + "Please try again."
                );

                statusLabel.setText(
                        "Unexpected application error."
                );

                e.printStackTrace();

            } finally {

                setSearchingState(false);
            }
        }
    };

    worker.execute();
}


    // =========================================================
    // SEARCHING STATE
    // =========================================================

    /**
     * Enables or disables controls while an API
     * request is being processed.
     *
     * @param searching true while searching
     */
    private void setSearchingState(
            boolean searching) {

        searchButton.setEnabled(
                !searching
        );

        locationInput
                .getField()
                .setEnabled(
                        !searching
                );

        temperatureUnitBox.setEnabled(
                !searching
        );

        windUnitBox.setEnabled(
                !searching
        );

        if (searching) {

            searchButton.setText(
                    "Searching..."
            );

        } else {

            searchButton.setText(
                    "Search"
            );
        }
    }

    // =========================================================
    // CURRENT WEATHER DISPLAY
    // =========================================================

    /**
     * Updates the current weather section.
     */
    private void updateCurrentWeather() {

        if (currentWeather == null) {
            return;
        }

        cityLabel.setText(
                currentWeather.getCity()
        );

        double temperature =
                convertTemperature(
                        currentWeather
                                .getTemperature()
                );

        double feelsLike =
                convertTemperature(
                        currentWeather
                                .getFeelsLike()
                );

        temperatureLabel.setText(
                String.format(
                        "Temperature: %.1f %s",
                        temperature,
                        getTemperatureUnit()
                )
        );

        feelsLikeLabel.setText(
                String.format(
                        "Feels Like: %.1f %s",
                        feelsLike,
                        getTemperatureUnit()
                )
        );

        conditionLabel.setText(
                "Condition: "
                        + capitalize(
                                currentWeather
                                        .getCondition()
                        )
        );

        humidityLabel.setText(
                "Humidity: "
                        + currentWeather
                                .getHumidity()
                        + "%"
        );

        double wind =
                convertWindSpeed(
                        currentWeather
                                .getWindSpeed()
                );

        windLabel.setText(
                String.format(
                        "Wind Speed: %.1f %s",
                        wind,
                        getWindUnit()
                )
        );

        pressureLabel.setText(
                "Pressure: "
                        + currentWeather
                                .getPressure()
                        + " hPa"
        );

        visibilityLabel.setText(
                String.format(
                        "Visibility: %.1f km",
                        currentWeather
                                .getVisibility()
                )
        );

        loadWeatherIcon(
                currentWeather
                        .getIconCode(),
                weatherIconLabel
        );
    }

    // =========================================================
    // FORECAST
    // =========================================================

    /**
     * Updates the forecast section.
     */
    private void updateForecast() {

        if (currentForecast == null) {
            return;
        }

        forecastPanel.removeAll();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "EEE HH:mm"
                );

        int numberOfCards =
                Math.min(
                        4,
                        currentForecast.size()
                );

        for (int i = 0;
             i < numberOfCards;
             i++) {

            forecastPanel.add(
                    createForecastCard(
                            currentForecast.get(i),
                            formatter
                    )
            );
        }

        for (int i = numberOfCards;
             i < 4;
             i++) {

            forecastPanel.add(
                    createEmptyForecastCard()
            );
        }

        forecastPanel.revalidate();

        forecastPanel.repaint();
    }

    /**
     * Creates an empty forecast card.
     *
     * @return empty forecast card
     */
    private JPanel createEmptyForecastCard() {

        JPanel card =
                new JPanel();

        card.setLayout(
                new javax.swing.BoxLayout(
                        card,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createEtchedBorder()
        );

        JLabel date =
                createCenteredLabel(
                        "--",
                        13,
                        Font.PLAIN
                );

        JLabel icon =
                createCenteredLabel(
                        "--",
                        13,
                        Font.PLAIN
                );

        JLabel temperature =
                createCenteredLabel(
                        "-- °C",
                        18,
                        Font.BOLD
                );

        JLabel condition =
                createCenteredLabel(
                        "--",
                        13,
                        Font.PLAIN
                );

        card.add(date);
        card.add(icon);
        card.add(temperature);
        card.add(condition);

        return card;
    }

    /**
     * Creates a forecast card containing forecast data.
     *
     * @param forecast forecast information
     * @param formatter date formatter
     * @return forecast card
     */
    private JPanel createForecastCard(
            ForecastData forecast,
            DateTimeFormatter formatter) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new javax.swing.BoxLayout(
                        card,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createEtchedBorder()
        );

        JLabel dateLabel =
                createCenteredLabel(
                        forecast
                                .getDateTime()
                                .format(
                                        formatter
                                ),
                        13,
                        Font.PLAIN
                );

        JLabel iconLabel =
                createCenteredLabel(
                        "",
                        13,
                        Font.PLAIN
                );

        double temperature =
                convertTemperature(
                        forecast
                                .getTemperature()
                );

        JLabel temperatureLabel =
                createCenteredLabel(
                        String.format(
                                "%.1f %s",
                                temperature,
                                getTemperatureUnit()
                        ),
                        18,
                        Font.BOLD
                );

        JLabel conditionLabel =
                createCenteredLabel(
                        capitalize(
                                forecast
                                        .getCondition()
                        ),
                        13,
                        Font.PLAIN
                );

        double wind =
                convertWindSpeed(
                        forecast
                                .getWindSpeed()
                );

        JLabel windLabel =
                createCenteredLabel(
                        String.format(
                                "Wind: %.1f %s",
                                wind,
                                getWindUnit()
                        ),
                        12,
                        Font.PLAIN
                );

        loadWeatherIcon(
                forecast.getIconCode(),
                iconLabel
        );

        card.add(dateLabel);
        card.add(iconLabel);
        card.add(temperatureLabel);
        card.add(conditionLabel);
        card.add(windLabel);

        return card;
    }

    // =========================================================
    // HISTORY
    // =========================================================

    /**
     * Updates the history list shown in the GUI.
     */
    private void updateHistoryList() {

        historyListModel.clear();

        List<WeatherHistory.HistoryEntry>
                entries =
                weatherHistory.getHistory();

        for (
                WeatherHistory.HistoryEntry entry
                : entries) {

            String historyText =
                    String.format(
                            "<html><b>%s</b><br>"
                                    + "%.1f°C | %s<br>"
                                    + "%s</html>",
                            entry.getCity(),
                            entry.getTemperature(),
                            capitalize(
                                    entry.getCondition()
                            ),
                            entry.getFormattedTimestamp()
                    );

            historyListModel.addElement(
                    historyText
            );
        }
    }

    /**
     * Clears search history after user confirmation.
     */
    private void clearHistory() {

        if (weatherHistory.size() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "There is no search history to clear.",
                    "Search History",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to clear "
                                + "all recent searches?",
                        "Clear Search History",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice ==
                JOptionPane.YES_OPTION) {

            weatherHistory.clearHistory();

            updateHistoryList();

            statusLabel.setText(
                    "Search history cleared."
            );
        }
    }

    // =========================================================
    // UNIT CONVERSION
    // =========================================================

    /**
     * Converts Celsius according to the selected unit.
     *
     * @param celsius temperature in Celsius
     * @return converted temperature
     */
    private double convertTemperature(
            double celsius) {

        String selected =
                (String)
                        temperatureUnitBox
                                .getSelectedItem();

        if ("Fahrenheit (°F)"
                .equals(selected)) {

            return UnitConverter
                    .celsiusToFahrenheit(
                            celsius
                    );
        }

        return celsius;
    }

    /**
     * Converts wind speed according to the selected unit.
     *
     * @param metersPerSecond wind speed
     * @return converted wind speed
     */
    private double convertWindSpeed(
            double metersPerSecond) {

        String selected =
                (String)
                        windUnitBox
                                .getSelectedItem();

        if ("km/h".equals(selected)) {

            return UnitConverter
                    .metersPerSecondToKilometersPerHour(
                            metersPerSecond
                    );
        }

        if ("mph".equals(selected)) {

            return UnitConverter
                    .metersPerSecondToMilesPerHour(
                            metersPerSecond
                    );
        }

        return metersPerSecond;
    }

    /**
     * Gets the selected temperature unit.
     *
     * @return temperature unit
     */
    private String getTemperatureUnit() {

        if ("Fahrenheit (°F)"
                .equals(
                        temperatureUnitBox
                                .getSelectedItem()
                )) {

            return "°F";
        }

        return "°C";
    }

    /**
     * Gets the selected wind unit.
     *
     * @return wind unit
     */
    private String getWindUnit() {

        String selected =
                (String)
                        windUnitBox
                                .getSelectedItem();

        return selected == null
                ? "m/s"
                : selected;
    }

    /**
     * Refreshes weather values after changing units.
     */
    private void refreshUnits() {

        if (currentWeather == null) {
            return;
        }

        updateCurrentWeather();

        updateForecast();

        statusLabel.setText(
                "Display units changed."
        );
    }

    // =========================================================
    // WEATHER ICON
    // =========================================================

    /**
     * Loads an OpenWeatherMap weather icon.
     *
     * @param iconCode API icon code
     * @param label label that receives the icon
     */
    private void loadWeatherIcon(
            String iconCode,
            JLabel label) {

        if (iconCode == null
                || iconCode.isBlank()
                || "Unknown".equals(iconCode)) {

            label.setIcon(null);

            label.setText(
                    "N/A"
            );

            return;
        }

        try {

            String iconURL =
                    "https://openweathermap.org/img/wn/"
                            + iconCode
                            + "@2x.png";

            ImageIcon icon =
                    new ImageIcon(
                            new URL(iconURL)
                    );

            Image image =
                    icon.getImage()
                            .getScaledInstance(
                                    80,
                                    80,
                                    Image.SCALE_SMOOTH
                            );

            label.setIcon(
                    new ImageIcon(image)
            );

            label.setText("");

        } catch (Exception e) {

            label.setIcon(null);

            label.setText(
                    "N/A"
            );
        }
    }

    // =========================================================
    // DYNAMIC BACKGROUND
    // =========================================================

    /**
     * Applies the background and foreground colors
     * based on the current time of day.
     */
    private void applyDynamicBackground() {

        Color background =
                DynamicBackground
                        .getBackgroundColor();

        Color foreground =
                DynamicBackground
                        .getForegroundColor();

        updateComponentColors(
                getContentPane(),
                background,
                foreground
        );

        if (timePeriodLabel != null) {

            timePeriodLabel.setText(
                    DynamicBackground
                            .getTimePeriod()
            );

            timePeriodLabel.setForeground(
                    foreground
            );
        }

        repaint();
    }

    /**
     * Recursively updates Swing component colors.
     *
     * @param component component to update
     * @param background background color
     * @param foreground foreground color
     */
    private void updateComponentColors(
            Component component,
            Color background,
            Color foreground) {

        if (component instanceof JPanel) {

            JPanel panel =
                    (JPanel) component;

            panel.setBackground(
                    background
            );
        }

        if (component instanceof JLabel) {

            JLabel label =
                    (JLabel) component;

            label.setForeground(
                    foreground
            );
        }

        if (component instanceof JButton) {

            JButton button =
                    (JButton) component;

            button.setForeground(
                    foreground
            );
        }

        if (component instanceof JComboBox) {

            JComboBox<?> comboBox =
                    (JComboBox<?>) component;

            comboBox.setForeground(
                    foreground
            );
        }

        if (component instanceof JScrollPane) {

            JScrollPane scrollPane =
                    (JScrollPane) component;

            scrollPane.getViewport()
                    .setBackground(
                            background
                    );
        }

        if (component instanceof Container) {

            Container container =
                    (Container) component;

            for (
                    Component child
                    : container.getComponents()) {

                updateComponentColors(
                        child,
                        background,
                        foreground
                );
            }
        }
    }

    /**
     * Starts the timer responsible for updating
     * the background every minute.
     */
    private void startBackgroundTimer() {

        backgroundTimer =
                new Timer(
                        60000,
                        event ->
                                applyDynamicBackground()
                );

        backgroundTimer.setRepeats(
                true
        );

        backgroundTimer.start();
    }

    // =========================================================
    // ERROR HANDLING
    // =========================================================

    /**
     * Displays an error dialog.
     *
     * @param message error message
     */
    private void showError(
            String message) {

        if (message == null
                || message.isBlank()) {

            message =
                    "Unable to retrieve weather information.";
        }

        JOptionPane.showMessageDialog(
                this,
                message,
                "Weather Information Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Displays an input validation warning.
     *
     * @param message warning message
     */
    private void showWarning(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    /**
     * Capitalizes the first character of a string.
     *
     * @param value original string
     * @return capitalized string
     */
    private String capitalize(
            String value) {

        if (value == null
                || value.isEmpty()) {

            return "--";
        }

        return value.substring(0, 1)
                .toUpperCase()
                + value.substring(1);
    }

    // =========================================================
    // WEATHER RESULT
    // =========================================================

    /**
     * Combines current weather and forecast results
     * returned by the background operation.
     */
    private static class WeatherResult {

        private final WeatherData currentWeather;

        private final List<ForecastData> forecast;

        /**
         * Creates a WeatherResult.
         *
         * @param currentWeather current weather
         * @param forecast forecast information
         */
        private WeatherResult(
                WeatherData currentWeather,
                List<ForecastData> forecast) {

            this.currentWeather =
                    currentWeather;

            this.forecast =
                    forecast;
        }
    }

    // =========================================================
    // TEXT FIELD HELPER
    // =========================================================

    /**
     * Small wrapper used to keep the location input
     * component isolated from the rest of the GUI.
     */
    private static class JTextFieldWrapper {

        private final javax.swing.JTextField field;

        /**
         * Creates the text field wrapper.
         *
         * @param columns number of text-field columns
         */
        private JTextFieldWrapper(
                int columns) {

            field =
                    new javax.swing.JTextField(
                            columns
                    );
        }

        /**
         * Returns the underlying JTextField.
         *
         * @return text field
         */
        private javax.swing.JTextField
        getField() {

            return field;
        }
    }
}