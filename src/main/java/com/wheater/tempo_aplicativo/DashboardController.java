package com.wheater.tempo_aplicativo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private void handleHome(ActionEvent event) {
        loadView("hello-view.fxml");
    }

    @FXML
    private void handleTempoReal(ActionEvent event) {
        // Load Tempo Real view
    }

    @FXML
    private void handleRelatorio(ActionEvent event) {
        // Load Relatório view
    }

    @FXML
    private void handleSair(ActionEvent event) {
        // Implement logout functionality
    }

    private void loadView(String fxml) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxml));
            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
