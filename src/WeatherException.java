/**
 * Custom exception used for weather-related errors.
 *
 * This class allows the application to distinguish
 * weather API and weather-processing errors from
 * ordinary Java exceptions.
 */
public class WeatherException extends Exception {

    /**
     * Creates a WeatherException with a message.
     *
     * @param message description of the error
     */
    public WeatherException(String message) {
        super(message);
    }

    /**
     * Creates a WeatherException with a message
     * and the original exception as the cause.
     *
     * @param message description of the error
     * @param cause original exception
     */
    public WeatherException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}