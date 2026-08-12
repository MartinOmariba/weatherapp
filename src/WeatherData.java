/**
 * Represents current weather information returned
 * by the weather API.
 *
 * This class acts as a data model for the application.
 */
public class WeatherData {

    private final String city;
    private final double temperature;
    private final double feelsLike;
    private final int humidity;
    private final double windSpeed;
    private final int pressure;
    private final double visibility;
    private final String condition;
    private final String iconCode;

    /**
     * Creates a WeatherData object.
     *
     * @param city city or location name
     * @param temperature current temperature in Celsius
     * @param feelsLike apparent temperature in Celsius
     * @param humidity humidity percentage
     * @param windSpeed wind speed in meters per second
     * @param pressure atmospheric pressure in hPa
     * @param visibility visibility in kilometers
     * @param condition weather description
     * @param iconCode OpenWeatherMap icon code
     */
    public WeatherData(
            String city,
            double temperature,
            double feelsLike,
            int humidity,
            double windSpeed,
            int pressure,
            double visibility,
            String condition,
            String iconCode) {

        this.city = city;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.visibility = visibility;
        this.condition = condition;
        this.iconCode = iconCode;
    }

    /**
     * Gets the city name.
     *
     * @return city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the temperature in Celsius.
     *
     * @return temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets the apparent temperature in Celsius.
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
     * Gets wind speed in meters per second.
     *
     * @return wind speed
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets atmospheric pressure.
     *
     * @return pressure in hPa
     */
    public int getPressure() {
        return pressure;
    }

    /**
     * Gets visibility.
     *
     * @return visibility in kilometers
     */
    public double getVisibility() {
        return visibility;
    }

    /**
     * Gets the weather condition.
     *
     * @return weather condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets the weather icon code.
     *
     * @return icon code
     */
    public String getIconCode() {
        return iconCode;
    }
}