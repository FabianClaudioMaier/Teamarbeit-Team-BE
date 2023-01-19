package com.example.demo;

import com.example.demo.Models.Array;
import com.example.demo.Models.Coordinates;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
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
    private boolean RUNNING = false;
    private boolean EDITING = false;
    private Group ROOT = new Group();
    private Scene SCENE = new Scene(ROOT, WIDTH + 100, HEIGHT + 100);
    private static Array ARRAY = new Array(ROWS,COLUMNS);
    private static Timer TIMER = new Timer();

    private static int timeStamp = 0;

    private static int simulationSpeed = 20;
    private static List<Array> SAVED = new ArrayList<>();

    private static ArrayGameHandler arrayGameHandler = new ArrayGameHandler(ARRAY);


    public static void main(String[] args) {
        ARRAY.create(0.5);
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
                arrayGameHandler.nextStep();
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLUMNS; col++) {
                        ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
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
                if (RUNNING) StartStop.setText("Start");
                else StartStop.setText("Stop");

                RUNNING = !RUNNING;
                arrayGameHandler.playPause();
            }
        });

        Button Edit = new Button("Start Editing");
        Edit.setLayoutX(WIDTH + 10);
        Edit.setLayoutY(150);

        Edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!EDITING){
                    RUNNING = false;
                    TIMER.cancel();
                    StartStop.setText("Start");
                    Edit.setText("Stop Editing");
                }
                if(EDITING){
                    Edit.setText("Start Editing");
                }
                EDITING = !EDITING;
            }
        });

        Button Save = new Button("Save");
        Save.setLayoutX(WIDTH + 10);
        Save.setLayoutY(200);

        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save Button clicked");
                if(EDITING){
                    System.out.println("Saved");
                    SAVED.add(ARRAY);
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
                ARRAY = SAVED.get(0);
                updateCellApparell();
            }
        });

        createCells();

        ROOT.getChildren().add(nextStep);
        ROOT.getChildren().add(StartStop);
        ROOT.getChildren().add(Edit);
        ROOT.getChildren().add(Save);
        ROOT.getChildren().add(GoToArchive);

        primaryStage.setScene(SCENE);
        primaryStage.show();

        SCENE.setOnMousePressed( circleOnMouseDraggedEventHandler/*event -> {
            if(EDITING) {

                //List<Coordinates> coordinatesList = new ArrayList<>();
                int row = (int) (event.getY() / CELL_SIZE);
                int col = (int) (event.getX() / CELL_SIZE);
                //coordinatesList.add(new Coordinates(col,row));
                //arrayGameHandler.changeHoverCellsStatus(coordinatesList);
                arrayGameHandler.changeCellStatus(row,col);
                ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);

            } else {
                System.out.println("please Stop the Game to change states");
            }
        }*/);

        new AnimationTimer(){

            @Override
            public void handle(long now) {
                if(RUNNING && timeStamp%simulationSpeed == 0){
                    arrayGameHandler.nextStep();
                    timeStamp = 0;
                }

                timeStamp++;
                updateCellApparell();
            }
        }.start();
    }

    private void updateCellApparell(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private void createCells(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                ROOT.getChildren().add(cell);
            }
        }
    }

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    if(EDITING) {
                        List<Coordinates> coordinatesList = new ArrayList<>();
                        SCENE.setOnMouseMoved(event1 -> {
                            int row = (int) (event.getY() / CELL_SIZE);
                            int col = (int) (event.getX() / CELL_SIZE);
                            coordinatesList.add(new Coordinates(col,row));
                            ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                        });

                        arrayGameHandler.changeHoverCellsStatus(coordinatesList);
                        //arrayGameHandler.changeCellStatus(row,col);

                    } else {
                        System.out.println("please Stop the Game to change states");
                    }
                }
            };
}
