package com.wheater.tempo_aplicativo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextField cityTextField;

    @FXML
    private TableView<WeatherForecast> forecastTableView;

    @FXML
    private TableColumn<WeatherForecast, String> cityColumn;

    @FXML
    private TableColumn<WeatherForecast, String> temperatureColumn;

    @FXML
    private TableColumn<WeatherForecast, String> descriptionColumn;

    @FXML
    private TableColumn<WeatherForecast, String> minTemperatureColumn;

    @FXML
    private TableColumn<WeatherForecast, String> maxTemperatureColumn;

    @FXML
    private TableColumn<WeatherForecast, String> feelsLikeColumn;

    @FXML
    private TableColumn<WeatherForecast, String> pressureColumn;

    @FXML
    private TableColumn<WeatherForecast, String> humidityColumn;

    @FXML
    private TableColumn<WeatherForecast, String> windSpeedColumn;

    @FXML
    private TableColumn<WeatherForecast, String> windDirectionColumn;

    @FXML
    private TableColumn<WeatherForecast, String> visibilityColumn;

    private final DatabaseManager databaseManager = new DatabaseManager();

    @FXML
    public void initialize() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        minTemperatureColumn.setCellValueFactory(new PropertyValueFactory<>("minTemperature"));
        maxTemperatureColumn.setCellValueFactory(new PropertyValueFactory<>("maxTemperature"));
        feelsLikeColumn.setCellValueFactory(new PropertyValueFactory<>("feelsLike"));
        pressureColumn.setCellValueFactory(new PropertyValueFactory<>("pressure"));
        humidityColumn.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        windSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("windSpeed"));
        windDirectionColumn.setCellValueFactory(new PropertyValueFactory<>("windDirection"));
        visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));
    }

    @FXML
    protected void onGetWeatherForecastButtonClick() {
        String city = cityTextField.getText().trim();
        if (!city.isEmpty()) {
            JSONObject weatherData = WeatherService.getWeatherData(city);
            if (weatherData != null) {
                double temperatureValue = weatherData.getJSONObject("main").getDouble("temp");
                String temperature = temperatureValue + "°C";
                String description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                String minTemperature = weatherData.getJSONObject("main").getDouble("temp_min") + "°C";
                String maxTemperature = weatherData.getJSONObject("main").getDouble("temp_max") + "°C";
                String feelsLike = weatherData.getJSONObject("main").getDouble("feels_like") + "°C";
                String pressure = weatherData.getJSONObject("main").getInt("pressure") + " hPa";
                String humidity = weatherData.getJSONObject("main").getInt("humidity") + " %";
                String windSpeed = weatherData.getJSONObject("wind").getDouble("speed") + " m/s";
                String windDirection = weatherData.getJSONObject("wind").getInt("deg") + "°";
                String visibility = weatherData.getInt("visibility") + " m";

                WeatherForecast forecast = new WeatherForecast(city, temperature, description, minTemperature, maxTemperature, feelsLike, pressure, humidity, windSpeed, windDirection, visibility);

                // Inserir dados no banco de dados
                databaseManager.insertForecast(forecast);

                // Buscar e exibir previsões
                ObservableList<WeatherForecast> forecasts = FXCollections.observableArrayList(databaseManager.getForecasts(city));
                forecastTableView.setItems(forecasts);
            }
        }
    }

    public static class WeatherForecast {
        private final String city;
        private final String temperature;
        private final String description;
        private final String minTemperature;
        private final String maxTemperature;
        private final String feelsLike;
        private final String pressure;
        private final String humidity;
        private final String windSpeed;
        private final String windDirection;
        private final String visibility;

        public WeatherForecast(String city, String temperature, String description, String minTemperature, String maxTemperature, String feelsLike, String pressure, String humidity, String windSpeed, String windDirection, String visibility) {
            this.city = city;
            this.temperature = temperature;
            this.description = description;
            this.minTemperature = minTemperature;
            this.maxTemperature = maxTemperature;
            this.feelsLike = feelsLike;
            this.pressure = pressure;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.windDirection = windDirection;
            this.visibility = visibility;
        }

        public String getCity() {
            return city;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getDescription() {
            return description;
        }

        public String getMinTemperature() {
            return minTemperature;
        }

        public String getMaxTemperature() {
            return maxTemperature;
        }

        public String getFeelsLike() {
            return feelsLike;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public String getVisibility() {
            return visibility;
        }
    }

    public class DatabaseManager {
        public List<WeatherForecast> getForecasts(String city) {
            List<WeatherForecast> forecasts = new ArrayList<>();
            String query = "SELECT * FROM weather WHERE city = ? LIMIT 5";

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, city);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    WeatherForecast forecast = new WeatherForecast(
                            rs.getString("city"),
                            rs.getString("temperature"),
                            rs.getString("description"),
                            rs.getString("min_temperature"),
                            rs.getString("max_temperature"),
                            rs.getString("feels_like"),
                            rs.getString("pressure"),
                            rs.getString("humidity"),
                            rs.getString("wind_speed"),
                            rs.getString("wind_direction"),
                            rs.getString("visibility")
                    );
                    forecasts.add(forecast);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return forecasts;
        }

        public void insertForecast(WeatherForecast forecast) {
            String query = "INSERT INTO weather (city, temperature, description, min_temperature, max_temperature, feels_like, pressure, humidity, wind_speed, wind_direction, visibility) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, forecast.getCity());
                pstmt.setString(2, forecast.getTemperature());
                pstmt.setString(3, forecast.getDescription());
                pstmt.setString(4, forecast.getMinTemperature());
                pstmt.setString(5, forecast.getMaxTemperature());
                pstmt.setString(6, forecast.getFeelsLike());
                pstmt.setString(7, forecast.getPressure());
                pstmt.setString(8, forecast.getHumidity());
                pstmt.setString(9, forecast.getWindSpeed());
                pstmt.setString(10, forecast.getWindDirection());
                pstmt.setString(11, forecast.getVisibility());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DatabaseUtil {
        private static final String URL = "jdbc:mysql://localhost:3306/weatherdb";
        private static final String USER = "root";
        private static final String PASSWORD = "entrar";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
}
