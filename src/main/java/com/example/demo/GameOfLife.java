package com.example.demo;

import javafx.animation.Timeline;
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
import java.util.Timer;
import java.util.TimerTask;

public class GameOfLife extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 60;
    private static final int COLUMNS = 80;
    private static final int CELL_SIZE = 10;
    private boolean gameStatePlay = false;

    private boolean editState = false;
    private Group root = new Group();
    private Scene scene = new Scene(root, WIDTH + 100, HEIGHT + 100);
    private static Array array = new Array(ROWS,COLUMNS);
    private final Timer timer = new Timer();

    private final TimerTask ttUpdateArray = new TimerTask() {
        @Override
        public void run() {
            array.update();
            //updateCellApparell();
            System.out.println(array);
        }
    };

    public static void main(String[] args) {
        array.create(0.5);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLife.class.getResource("Style.fxml"));

        Scene menu = new Scene(fxmlLoader.load(), 300,200);



        Button nextStep = new Button("next Step");
        nextStep.setLayoutX(WIDTH + 10);
        nextStep.setLayoutY(100);

        nextStep.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //updateGeneration();
                array.update();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(array.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });

        ToggleButton StartStop = new ToggleButton("Start");
        StartStop.setLayoutX(WIDTH + 10);
        StartStop.setLayoutY(200);

        StartStop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (gameStatePlay){
                    timer.cancel();
                    StartStop.setText("Start");
                }
                else {
                    timer.scheduleAtFixedRate(ttUpdateArray, 0, 500);
                    StartStop.setText("Stop");
                }
                gameStatePlay = !gameStatePlay;
            }
        });

        Button Edit = new Button("Edit");
        Edit.setLayoutX(WIDTH + 10);
        Edit.setLayoutY(300);

        Edit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                gameStatePlay = false;
                editState = !editState;
                timer.cancel();
                StartStop.setText("Start");

            }
        });


        //initializeCurrentGeneration();
        //array.create(0.5);

        updateCellApparell();

        root.getChildren().add(nextStep);
        root.getChildren().add(StartStop);
        root.getChildren().add(Edit);

        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setScene(menu);
        //primaryStage.show();

        scene.setOnMouseClicked(event -> {
            if(!gameStatePlay) {
                int row = (int) (event.getY() / CELL_SIZE);
                int col = (int) (event.getX() / CELL_SIZE);
                //currentGeneration[row][col] = !currentGeneration[row][col];
                array.setArray(row, col);
                ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(array.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            } else {
                System.out.println("please Stop the Game to change states");
            }
        });

        /*scene.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Space")) {
                updateGeneration();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(currentGeneration[row][col] ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });*/
    }

    private void initializeCurrentGeneration() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                //currentGeneration[row][col] = Math.random() < 0.5;
            }
        }
    }

    private void updateCellApparell(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setFill(array.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                root.getChildren().add(cell);
            }
        }
    }

    /*private void updateGeneration() {
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
    }*/
}
