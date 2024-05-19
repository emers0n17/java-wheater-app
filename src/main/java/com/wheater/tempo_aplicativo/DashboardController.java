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
        loadView("tempo-real-view.fxml");
    }

    @FXML
    private void handleRelatorio(ActionEvent event) {
        loadView("relatorio-view.fxml");
    }

    @FXML
    private void handleSair(ActionEvent event) {
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
