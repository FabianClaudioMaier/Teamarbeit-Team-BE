package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOfLife extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 60;
    private static final int COLUMNS = 80;
    private static final int CELL_SIZE = 10;

    private boolean[][] currentGeneration = new boolean[ROWS][COLUMNS];
    private boolean[][] nextGeneration = new boolean[ROWS][COLUMNS];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Group root = new Group();
        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLife.class.getResource("Style.fxml"));
        Scene scene = new Scene(root, WIDTH + 100, HEIGHT + 100);
        Scene menu = new Scene(fxmlLoader.load(), 300,200);

        Button nextStep = new Button("next Step");
        nextStep.setLayoutX(100);
        nextStep.setLayoutY(HEIGHT + 10);

        /*ToggleButton RunStop = new ToggleButton("Run");
        RunStop.setLayoutX(200);
        RunStop.setLayoutY(HEIGHT + 10);*/

        nextStep.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                updateGeneration();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });

       /* RunStop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(RunStop.)
                updateGeneration();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });*/



        initializeCurrentGeneration();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
                root.getChildren().add(cell);
            }
        }

        root.getChildren().add(nextStep);

        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setScene(menu);
        //primaryStage.show();

        scene.setOnMouseClicked(event -> {
            int row = (int) (event.getY() / CELL_SIZE);
            int col = (int) (event.getX() / CELL_SIZE);
            currentGeneration[row][col] = !currentGeneration[row][col];
            ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Space")) {
                updateGeneration();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });
    }

    private void initializeCurrentGeneration() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                currentGeneration[row][col] = Math.random() < 0.5;
            }
        }
    }

    private void updateGeneration() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                nextGeneration[row][col] = shouldLive(row, col);
            }
        }
        currentGeneration = nextGeneration;
        nextGeneration = new boolean[ROWS][COLUMNS];
    }

    private boolean shouldLive(int row, int col) {
        int neighbors = countNeighbors(row, col);
        if (currentGeneration[row][col]) {
            return neighbors == 2 || neighbors == 3;
        } else {
            return neighbors == 3;
        }
    }

    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < ROWS && j >= 0 && j < COLUMNS && (i != row || j != col)) {
                    count += currentGeneration[i][j] ? 1 : 0;
                }
            }
        }
        return count;
    }
}
