package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class HelloController {
    @FXML
    private Label welcomeText;

    //protected void simulation(){ GameOfLife.main();}

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onGoodbyeButtonClick() {welcomeText.setText("Goodbye!");}
}