import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeatherApp {

    private static final String API_KEY = "260c6f2593b796d67e5c33eefdb9e621"; 
    private static final int MAX_FAVOURITES = 3;

    private static final List<String> favourites = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== WEATHER APP ===");
            System.out.println("1. Search Weather");
            System.out.println("2. Add to Favourites");
            System.out.println("3. List Favourites");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": searchWeather(); break;
                case "2": addFavourite();  break;
                case "3": showFavourites(); break;
                case "4":
                    System.out.println("Bye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void searchWeather() {
        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();
        System.out.println(getWeather(city));
    }

    private static void addFavourite() {
        System.out.print("Enter city to add: ");
        String city = scanner.nextLine().trim();

        if (favourites.contains(city)) {
            System.out.println("Already in favourites.");
            return;
        }

        if (favourites.size() < MAX_FAVOURITES) {
            favourites.add(city);
            System.out.println("Added.");
        } else {
            System.out.println("Favourites full: " + favourites);
            System.out.print("Remove a city: ");
            String remove = scanner.nextLine().trim();

            if (favourites.remove(remove)) {
                favourites.add(city);
                System.out.println("Updated.");
            } else {
                System.out.println("City not found.");
            }
        }
    }

    private static void showFavourites() {
        if (favourites.isEmpty()) {
            System.out.println("No favourites yet.");
            return;
        }

        for (String city : favourites) {
            System.out.println(city + " → " + getWeather(city));
        }
    }

    private static String getWeather(String city) {
        HttpURLConnection conn = null;

        try {
            String query = "q=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                    + "&appid=" + API_KEY
                    + "&units=metric";

            URI uri = new URI("https", "api.openweathermap.org",
                    "/data/2.5/weather", query, null);
            URL url = uri.toURL();

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();

            BufferedReader reader;
            if (status == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while (reader != null && (line = reader.readLine()) != null) {
                sb.append(line);
            }
            if (reader != null) reader.close();

            String body = sb.toString();

            if (status != HttpURLConnection.HTTP_OK) {
                return "Error from API: HTTP " + status;
            }

            String temp = extract(body, "\"temp\":", ",");
            String desc = extract(body, "\"description\":\"", "\"");

            return temp + "°C, " + desc;

        } catch (Exception e) {
            return "Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String extract(String text, String start, String end) {
        int s = text.indexOf(start);
        if (s == -1) return "N/A";
        s += start.length();
        int e = text.indexOf(end, s);
        if (e == -1) return "N/A";
        return text.substring(s, e);
    }
}
