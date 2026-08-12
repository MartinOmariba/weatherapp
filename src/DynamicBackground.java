
import java.awt.Color;
import java.time.LocalTime;

/**
 * Controls the application's background appearance
 * according to the current time of day.
 *
 * Time periods:
 *
 * Morning   - 06:00 to 11:59
 * Afternoon - 12:00 to 16:59
 * Evening   - 17:00 to 20:59
 * Night     - 21:00 to 05:59
 */
public final class DynamicBackground {

    /**
     * Private constructor because this is a
     * utility class.
     */
    private DynamicBackground() {
    }

    /**
     * Determines the appropriate background color
     * for the current time.
     *
     * @return background color
     */
    public static Color getBackgroundColor() {

        int hour =
                LocalTime.now().getHour();

        if (hour >= 6 && hour < 12) {

            // Morning
            return new Color(
                    220,
                    235,
                    250
            );
        }

        if (hour >= 12 && hour < 17) {

            // Afternoon
            return new Color(
                    235,
                    245,
                    255
            );
        }

        if (hour >= 17 && hour < 21) {

            // Evening / sunset
            return new Color(
                    245,
                    220,
                    190
            );
        }

        // Night
        return new Color(
                35,
                45,
                65
        );
    }

    /**
     * Gets a textual description of the current
     * time period.
     *
     * @return Morning, Afternoon, Evening, or Night
     */
    public static String getTimePeriod() {

        int hour =
                LocalTime.now().getHour();

        if (hour >= 6 && hour < 12) {
            return "Morning";
        }

        if (hour >= 12 && hour < 17) {
            return "Afternoon";
        }

        if (hour >= 17 && hour < 21) {
            return "Evening";
        }

        return "Night";
    }

    /**
     * Determines an appropriate text color.
     *
     * @return foreground color
     */
    public static Color getForegroundColor() {

        if ("Night".equals(
                getTimePeriod())) {

            return Color.WHITE;
        }

        return Color.DARK_GRAY;
    }
}