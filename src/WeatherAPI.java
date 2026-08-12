import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles communication with the OpenWeatherMap API.
 *
 * The class retrieves:
 *
 * - Current weather
 * - Temperature
 * - Feels-like temperature
 * - Humidity
 * - Wind speed
 * - Atmospheric pressure
 * - Visibility
 * - Weather condition
 * - Weather icons
 * - Short-term forecast
 *
 * The API key is read from the environment variable:
 *
 * OPENWEATHER_API_KEY
 */
public class WeatherAPI {

    /**
     * API key obtained from the environment.
     *
     * The key is deliberately not hard-coded into
     * the source code.
     */
    private static final String API_KEY =
            System.getenv(
                    "OPENWEATHER_API_KEY"
            );

    /**
     * OpenWeatherMap current weather endpoint.
     */
    private static final String CURRENT_WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    /**
     * OpenWeatherMap five-day forecast endpoint.
     */
    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast";

    /**
     * HTTP client used for API communication.
     */
    private final HttpClient client;

    /**
     * Creates the API client.
     */
    public WeatherAPI() {

        client =
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(10)
                        )
                        .build();
    }

    /**
     * Retrieves current weather information.
     *
     * @param city city name entered by the user
     * @return current weather data
     * @throws WeatherException when the request fails
     */
    public WeatherData getWeather(
            String city)
            throws WeatherException {

        validateLocation(city);
        checkApiKey();

        String encodedCity =
                URLEncoder.encode(
                        city.trim(),
                        StandardCharsets.UTF_8
                );

        String url =
                CURRENT_WEATHER_URL
                        + "?q="
                        + encodedCity
                        + "&units=metric"
                        + "&appid="
                        + API_KEY;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .timeout(
                                Duration.ofSeconds(15)
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> response;

        try {

            response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );

        } catch (HttpTimeoutException e) {

            throw new WeatherException(
                    "The weather service took too long "
                            + "to respond. Please try again.",
                    e
            );

        } catch (ConnectException e) {

            throw new WeatherException(
                    "Unable to connect to the weather service. "
                            + "Please check your internet connection.",
                    e
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new WeatherException(
                    "The weather request was interrupted.",
                    e
            );

        } catch (IOException e) {

            throw new WeatherException(
                    "A network error occurred while retrieving "
                            + "weather information.",
                    e
            );
        }

        checkResponse(
                response.statusCode()
        );

        return parseWeatherResponse(
                response.body()
        );
    }

    /**
     * Retrieves short-term forecast information.
     *
     * @param city city name
     * @return forecast entries
     * @throws WeatherException when the request fails
     */
    public List<ForecastData> getForecast(
            String city)
            throws WeatherException {

        validateLocation(city);
        checkApiKey();

        String encodedCity =
                URLEncoder.encode(
                        city.trim(),
                        StandardCharsets.UTF_8
                );

        String url =
                FORECAST_URL
                        + "?q="
                        + encodedCity
                        + "&units=metric"
                        + "&appid="
                        + API_KEY;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .timeout(
                                Duration.ofSeconds(15)
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> response;

        try {

            response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );

        } catch (HttpTimeoutException e) {

            throw new WeatherException(
                    "The forecast request timed out. "
                            + "Please try again.",
                    e
            );

        } catch (ConnectException e) {

            throw new WeatherException(
                    "Unable to connect to the forecast service. "
                            + "Please check your internet connection.",
                    e
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new WeatherException(
                    "The forecast request was interrupted.",
                    e
            );

        } catch (IOException e) {

            throw new WeatherException(
                    "A network error occurred while retrieving "
                            + "the forecast.",
                    e
            );
        }

        checkResponse(
                response.statusCode()
        );

        return parseForecastResponse(
                response.body()
        );
    }

    /**
     * Validates the location entered by the user.
     *
     * @param city location entered by the user
     * @throws WeatherException if the location is invalid
     */
    private void validateLocation(
            String city)
            throws WeatherException {

        if (city == null
                || city.trim().isEmpty()) {

            throw new WeatherException(
                    "Please enter a location."
            );
        }

        if (city.trim().length() < 2) {

            throw new WeatherException(
                    "The location must contain at least "
                            + "two characters."
            );
        }

        if (city.trim().length() > 100) {

            throw new WeatherException(
                    "The location name is too long."
            );
        }
    }

    /**
     * Checks whether an API key is available.
     *
     * @throws WeatherException if the key is missing
     */
    private void checkApiKey()
            throws WeatherException {

        if (API_KEY == null
                || API_KEY.isBlank()) {

            throw new WeatherException(
                    "OpenWeather API key is not configured. "
                            + "Please set the OPENWEATHER_API_KEY "
                            + "environment variable."
            );
        }
    }

    /**
     * Handles HTTP status codes returned by the API.
     *
     * @param statusCode HTTP status code
     * @throws WeatherException for unsuccessful responses
     */
    private void checkResponse(
            int statusCode)
            throws WeatherException {

        switch (statusCode) {

            case 200:

                return;

            case 401:

                throw new WeatherException(
                        "The weather API key is invalid "
                                + "or unauthorized."
                );

            case 404:

                throw new WeatherException(
                        "Location not found. Please check "
                                + "the city name and try again."
                );

            case 429:

                throw new WeatherException(
                        "Too many weather requests have been "
                                + "made. Please try again later."
                );

            case 500:
            case 502:
            case 503:
            case 504:

                throw new WeatherException(
                        "The weather service is temporarily "
                                + "unavailable. Please try again later."
                );

            default:

                throw new WeatherException(
                        "The weather service returned HTTP status "
                                + statusCode
                );
        }
    }

    /**
     * Parses current weather information.
     *
     * This implementation extracts the required fields
     * from the JSON response without requiring an
     * additional external JSON library.
     *
     * @param json API response
     * @return weather data
     * @throws WeatherException if required data is missing
     */
    private WeatherData parseWeatherResponse(
            String json)
            throws WeatherException {

        if (json == null
                || json.isBlank()) {

            throw new WeatherException(
                    "The weather service returned an empty response."
            );
        }

        try {

            String city =
                    extractString(
                            json,
                            "\"name\":\"",
                            "\""
                    );

            String condition =
                    extractString(
                            json,
                            "\"description\":\"",
                            "\""
                    );

            String iconCode =
                    extractString(
                            json,
                            "\"icon\":\"",
                            "\""
                    );

            double temperature =
                    extractRequiredDouble(
                            json,
                            "\"temp\":"
                    );

            double feelsLike =
                    extractRequiredDouble(
                            json,
                            "\"feels_like\":"
                    );

            int humidity =
                    (int) extractRequiredDouble(
                            json,
                            "\"humidity\":"
                    );

            double windSpeed =
                    extractRequiredDouble(
                            json,
                            "\"speed\":"
                    );

            int pressure =
                    (int) extractRequiredDouble(
                            json,
                            "\"pressure\":"
                    );

            double visibility =
                    extractRequiredDouble(
                            json,
                            "\"visibility\":"
                    );

            /*
             * OpenWeatherMap returns visibility in meters.
             * The application displays it in kilometers.
             */
            visibility =
                    visibility / 1000.0;

            return new WeatherData(
                    city,
                    temperature,
                    feelsLike,
                    humidity,
                    windSpeed,
                    pressure,
                    visibility,
                    condition,
                    iconCode
            );

        } catch (WeatherException e) {

            throw e;

        } catch (Exception e) {

            throw new WeatherException(
                    "The weather service returned unexpected data.",
                    e
            );
        }
    }

    /**
     * Parses forecast data.
     *
     * @param json API response
     * @return list of forecast entries
     * @throws WeatherException if forecast data is unavailable
     */
    private List<ForecastData> parseForecastResponse(
            String json)
            throws WeatherException {

        if (json == null
                || json.isBlank()) {

            throw new WeatherException(
                    "The forecast service returned an empty response."
            );
        }

        List<ForecastData> forecasts =
                new ArrayList<>();

        try {

            int listStart =
                    json.indexOf("\"list\":[");

            if (listStart == -1) {

                throw new WeatherException(
                        "The forecast response does not contain "
                                + "valid forecast data."
                );
            }

            String list =
                    json.substring(listStart);

            /*
             * The API normally returns forecast entries
             * beginning with {"dt":.
             */
            String[] entries =
                    list.split("\\{\"dt\":");

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd HH:mm:ss"
                    );

            /*
             * Four forecast cards are displayed in the GUI.
             * Eight entries are retained here so the data
             * remains available for future enhancements.
             */
            int numberOfEntries =
                    Math.min(
                            8,
                            entries.length
                    );

            for (int i = 1;
                 i < numberOfEntries;
                 i++) {

                try {

                    String entry =
                            "{\"dt\":"
                                    + entries[i];

                    String dateTimeText =
                            extractString(
                                    entry,
                                    "\"dt_txt\":\"",
                                    "\""
                            );

                    if ("Unknown".equals(
                            dateTimeText)) {

                        continue;
                    }

                    LocalDateTime dateTime =
                            LocalDateTime.parse(
                                    dateTimeText,
                                    formatter
                            );

                    double temperature =
                            extractRequiredDouble(
                                    entry,
                                    "\"temp\":"
                            );

                    double feelsLike =
                            extractRequiredDouble(
                                    entry,
                                    "\"feels_like\":"
                            );

                    int humidity =
                            (int) extractRequiredDouble(
                                    entry,
                                    "\"humidity\":"
                            );

                    double windSpeed =
                            extractRequiredDouble(
                                    entry,
                                    "\"speed\":"
                            );

                    String condition =
                            extractString(
                                    entry,
                                    "\"description\":\"",
                                    "\""
                            );

                    String iconCode =
                            extractString(
                                    entry,
                                    "\"icon\":\"",
                                    "\""
                            );

                    forecasts.add(
                            new ForecastData(
                                    dateTime,
                                    temperature,
                                    feelsLike,
                                    humidity,
                                    windSpeed,
                                    condition,
                                    iconCode
                            )
                    );

                } catch (Exception ignored) {

                    /*
                     * A malformed individual forecast
                     * entry does not prevent the remaining
                     * forecast entries from being displayed.
                     */
                }
            }

            if (forecasts.isEmpty()) {

                throw new WeatherException(
                        "No usable forecast information was returned."
                );
            }

            return forecasts;

        } catch (WeatherException e) {

            throw e;

        } catch (Exception e) {

            throw new WeatherException(
                    "Unable to process the forecast data.",
                    e
            );
        }
    }

    /**
     * Extracts a string value from JSON.
     *
     * @param json JSON response
     * @param start starting marker
     * @param end ending marker
     * @return extracted string
     */
    private String extractString(
            String json,
            String start,
            String end) {

        int startIndex =
                json.indexOf(start);

        if (startIndex == -1) {

            return "Unknown";
        }

        startIndex += start.length();

        int endIndex =
                json.indexOf(
                        end,
                        startIndex
                );

        if (endIndex == -1) {

            return "Unknown";
        }

        return json.substring(
                startIndex,
                endIndex
        );
    }

    /**
     * Extracts a required numeric value from JSON.
     *
     * @param json JSON response
     * @param key numeric JSON key
     * @return numeric value
     * @throws WeatherException if the value is invalid
     */
    private double extractRequiredDouble(
            String json,
            String key)
            throws WeatherException {

        int startIndex =
                json.indexOf(key);

        if (startIndex == -1) {

            throw new WeatherException(
                    "Required weather data is missing."
            );
        }

        startIndex += key.length();

        int endIndex =
                startIndex;

        while (endIndex < json.length()) {

            char character =
                    json.charAt(endIndex);

            if (!(Character.isDigit(character)
                    || character == '.'
                    || character == '-')) {

                break;
            }

            endIndex++;
        }

        try {

            return Double.parseDouble(
                    json.substring(
                            startIndex,
                            endIndex
                    )
            );

        } catch (NumberFormatException e) {

            throw new WeatherException(
                    "Invalid numerical weather data received.",
                    e
            );
        }
    }
}