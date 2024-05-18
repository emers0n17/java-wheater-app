package com.wheater.tempo_aplicativo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private final DatabaseManager databaseManager = new DatabaseManager();

    @FXML
    public void initialize() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    protected void onGetWeatherForecastButtonClick() {
        String city = cityTextField.getText().trim();
        if (!city.isEmpty()) {
            ObservableList<WeatherForecast> forecasts = FXCollections.observableArrayList(databaseManager.getForecasts(city));
            forecastTableView.setItems(forecasts);
        }
    }

    public static class WeatherForecast {
        private final String city;
        private final String temperature;
        private final String description;

        public WeatherForecast(String city, String temperature, String description) {
            this.city = city;
            this.temperature = temperature;
            this.description = description;
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
                            rs.getString("description")
                    );
                    forecasts.add(forecast);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return forecasts;
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
