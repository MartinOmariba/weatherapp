/**
 * Utility class responsible for converting weather
 * measurements between different units.
 *
 * OpenWeatherMap returns temperature in Celsius and
 * wind speed in meters per second in this application.
 */
public final class UnitConverter {

    /**
     * Private constructor prevents unnecessary
     * object creation.
     */
    private UnitConverter() {
    }

    /**
     * Converts Celsius to Fahrenheit.
     *
     * Formula:
     * F = (C × 9/5) + 32
     *
     * @param celsius temperature in Celsius
     * @return temperature in Fahrenheit
     */
    public static double celsiusToFahrenheit(
            double celsius) {

        return (celsius * 9.0 / 5.0) + 32.0;
    }

    /**
     * Converts meters per second to kilometers
     * per hour.
     *
     * @param metersPerSecond wind speed in m/s
     * @return wind speed in km/h
     */
    public static double metersPerSecondToKilometersPerHour(
            double metersPerSecond) {

        return metersPerSecond * 3.6;
    }

    /**
     * Converts meters per second to miles per hour.
     *
     * @param metersPerSecond wind speed in m/s
     * @return wind speed in mph
     */
    public static double metersPerSecondToMilesPerHour(
            double metersPerSecond) {

        return metersPerSecond * 2.236936;
    }
}