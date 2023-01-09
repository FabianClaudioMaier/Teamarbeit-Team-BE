package com.example.demo;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GameOfLife extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 15;
    private static final int COLUMNS = 20;
    private static final int CELL_SIZE = 40;
    private boolean gameStatePlay = false;
    private boolean editState = false;
    private Group root = new Group();
    private Scene scene = new Scene(root, WIDTH + 100, HEIGHT + 100);
    private static Array array = new Array(ROWS,COLUMNS);
    private static Timer timer = new Timer();

    private static int timeStamp = 0;
    private static List<Array> saved = new ArrayList<>();

    /*private final TimerTask ttUpdateArray = new TimerTask() {
        @Override
        public void run() {
            array.update();
        }
    };*/

    public static void main(String[] args) {
        array.create(0.5);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Button nextStep = new Button("next Step");
        nextStep.setLayoutX(WIDTH + 10);
        nextStep.setLayoutY(50);

        nextStep.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
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
        StartStop.setLayoutY(100);

        StartStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameStatePlay) StartStop.setText("Start");
                else StartStop.setText("Stop");

                gameStatePlay = !gameStatePlay;
            }
        });

        Button Edit = new Button("Start Editing");
        Edit.setLayoutX(WIDTH + 10);
        Edit.setLayoutY(150);

        Edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!editState){
                    gameStatePlay = false;
                    timer.cancel();
                    StartStop.setText("Start");
                    Edit.setText("Stop Editing");
                }
                if(editState){
                    Edit.setText("Start Editing");
                }
                editState = !editState;
            }
        });

        Button Save = new Button("Save");
        Save.setLayoutX(WIDTH + 10);
        Save.setLayoutY(200);

        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save Button clicked");
                if(editState){
                    System.out.println("Saved");
                    saved.add(array);
                } else {
                    System.out.println("you have to be in edit mode to save");
                }
            }
        });

        Button GoToArchive = new Button("Go to Archive");
        GoToArchive.setLayoutX(WIDTH + 10);
        GoToArchive.setLayoutY(250);

        GoToArchive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("pressed go to Archive");
                array = saved.get(0);
                updateCellApparell();
            }
        });

        updateCellApparell();

        root.getChildren().add(nextStep);
        root.getChildren().add(StartStop);
        root.getChildren().add(Edit);
        root.getChildren().add(Save);
        root.getChildren().add(GoToArchive);

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnMouseClicked(event -> {
            if(editState) {
                int row = (int) (event.getY() / CELL_SIZE);
                int col = (int) (event.getX() / CELL_SIZE);
                array.setArray(row, col);
                ((Rectangle) root.getChildren().get(row * COLUMNS + col)).setFill(array.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            } else {
                System.out.println("please Stop the Game to change states");
            }
        });
        new AnimationTimer(){

            @Override
            public void handle(long now) {
                if(gameStatePlay && timeStamp%5 == 0){
                    array.update();
                    timeStamp = 0;
                }

                timeStamp++;
                updateCellApparell();
            }
        }.start();
    }

    private void updateCellApparell(){
        List<Object> cells = Collections.singletonList(root.getChildren());
        for (Object cell: cells) {
            root.getChildren().remove(cell);
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setFill(array.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                root.getChildren().add(cell);
            }
        }
    }
}
