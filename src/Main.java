/**
 * Entry point for the Weather Information App.
 *
 * The application uses Java Swing to provide a graphical
 * user interface and retrieves weather information from
 * the OpenWeatherMap API.
 */
public class Main {

    /**
     * Starts the Weather Information App.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        /*
         * Swing applications should be created on the
         * Event Dispatch Thread.
         */
        javax.swing.SwingUtilities.invokeLater(() -> {

            WeatherApp application =
                    new WeatherApp();

            application.setVisible(true);
        });
    }
}