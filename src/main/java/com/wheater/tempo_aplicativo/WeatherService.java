package com.wheater.tempo_aplicativo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;  // Certifique-se de que esta importação está presente

public class WeatherService {
    private static final String API_KEY = "642f70887792287b566ca5fc70b324a1";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public static JSONObject getWeatherData(String city) {
        String urlString = String.format(BASE_URL, city, API_KEY);
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                return new JSONObject(inline.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
