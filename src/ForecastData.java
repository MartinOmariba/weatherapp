
import java.time.LocalDateTime;

/**
 * Represents a single weather forecast entry.
 *
 * Forecast information is retrieved from the
 * OpenWeatherMap forecast endpoint.
 */
public class ForecastData {

    private final LocalDateTime dateTime;
    private final double temperature;
    private final double feelsLike;
    private final int humidity;
    private final double windSpeed;
    private final String condition;
    private final String iconCode;

    /**
     * Creates a ForecastData object.
     *
     * @param dateTime forecast date and time
     * @param temperature forecast temperature in Celsius
     * @param feelsLike apparent temperature in Celsius
     * @param humidity humidity percentage
     * @param windSpeed wind speed in meters per second
     * @param condition weather condition
     * @param iconCode weather icon code
     */
    public ForecastData(
            LocalDateTime dateTime,
            double temperature,
            double feelsLike,
            int humidity,
            double windSpeed,
            String condition,
            String iconCode) {

        this.dateTime = dateTime;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.iconCode = iconCode;
    }

    /**
     * Gets the forecast date and time.
     *
     * @return date and time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets forecast temperature.
     *
     * @return temperature in Celsius
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets apparent temperature.
     *
     * @return feels-like temperature
     */
    public double getFeelsLike() {
        return feelsLike;
    }

    /**
     * Gets humidity.
     *
     * @return humidity percentage
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Gets wind speed.
     *
     * @return wind speed in meters per second
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets weather condition.
     *
     * @return condition description
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets weather icon code.
     *
     * @return icon code
     */
    public String getIconCode() {
        return iconCode;
    }
}