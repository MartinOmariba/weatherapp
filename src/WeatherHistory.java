import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maintains a list of the user's recent weather searches.
 *
 * The application stores a maximum of ten searches.
 * New searches are placed at the beginning of the list.
 */
public class WeatherHistory {

    /**
     * Maximum number of searches maintained.
     */
    private static final int MAX_HISTORY = 10;

    private final List<HistoryEntry> history;

    /**
     * Creates an empty weather history.
     */
    public WeatherHistory() {

        history =
                new ArrayList<>();
    }

    /**
     * Adds a successful weather search.
     *
     * @param weatherData weather information
     */
    public void addEntry(
            WeatherData weatherData) {

        if (weatherData == null) {
            return;
        }

        HistoryEntry entry =
                new HistoryEntry(
                        weatherData.getCity(),
                        weatherData.getTemperature(),
                        weatherData.getCondition(),
                        LocalDateTime.now()
                );

        /*
         * Newest searches are placed at index zero.
         */
        history.add(0, entry);

        /*
         * Remove the oldest entry when the
         * maximum history size is exceeded.
         */
        if (history.size() > MAX_HISTORY) {

            history.remove(
                    history.size() - 1
            );
        }
    }

    /**
     * Returns the search history.
     *
     * A copy is returned so external classes
     * cannot directly modify the internal list.
     *
     * @return unmodifiable history
     */
    public List<HistoryEntry> getHistory() {

        return Collections.unmodifiableList(
                new ArrayList<>(history)
        );
    }

    /**
     * Removes all history entries.
     */
    public void clearHistory() {

        history.clear();
    }

    /**
     * Gets the number of history entries.
     *
     * @return number of entries
     */
    public int size() {

        return history.size();
    }

    /**
     * Represents a single weather search.
     */
    public static class HistoryEntry {

        private final String city;
        private final double temperature;
        private final String condition;
        private final LocalDateTime timestamp;

        /**
         * Creates a history entry.
         *
         * @param city searched city
         * @param temperature temperature
         * @param condition weather condition
         * @param timestamp search timestamp
         */
        public HistoryEntry(
                String city,
                double temperature,
                String condition,
                LocalDateTime timestamp) {

            this.city = city;
            this.temperature = temperature;
            this.condition = condition;
            this.timestamp = timestamp;
        }

        public String getCity() {
            return city;
        }

        public double getTemperature() {
            return temperature;
        }

        public String getCondition() {
            return condition;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        /**
         * Formats the timestamp for display.
         *
         * @return formatted timestamp
         */
        public String getFormattedTimestamp() {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm:ss"
                    );

            return timestamp.format(formatter);
        }
    }
}