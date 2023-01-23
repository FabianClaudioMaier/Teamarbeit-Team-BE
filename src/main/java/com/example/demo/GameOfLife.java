package com.example.demo;

import com.example.demo.Models.Array;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private static Array ARRAY = new Array(ROWS, COLUMNS);
    private static Timer TIMER = new Timer();

    private static int timeStamp = 0;

    private static int simulationSpeed = 20;
    private static List<Array> SAVED = new ArrayList<>();

    private static GameArrayHandler gameArrayHandler = new GameArrayHandler(ARRAY);

    private int clickedCellState = 0;

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
                ARRAY.update();
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
            }
        });

        Button Edit = new Button("Start Editing");
        Edit.setLayoutX(WIDTH + 10);
        Edit.setLayoutY(150);

        Edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!EDITING) {
                    RUNNING = false;
                    TIMER.cancel();
                    StartStop.setText("Start");
                    Edit.setText("Stop Editing");
                }
                if (EDITING) {
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
                if (EDITING) {
                    System.out.println("Saved");
                    ArchiveDAL.save(ARRAY);
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


        SCENE.setOnMousePressed(event -> { //Event which changes the first state and appearance of the cell after it gets clicked
            if (EDITING) {
                try {
                    int row = (int) (event.getY() / CELL_SIZE);
                    int col = (int) (event.getX() / CELL_SIZE);
                    ARRAY.changeCellStatus(row, col);
                    ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                    clickedCellState = ARRAY.getArray()[row][col];
                } catch (Exception e) {
                    System.out.println("Please click a cell within the cell area!");
                }
            } else {
                System.out.println("Please enter the edit mode before editing!");
            }
        });

        SCENE.setOnMouseDragged(event -> { //Event which changes state and appearance of all cells getting dragged over, based on state of first clicked cell
            if (EDITING) {
                try {
                    int row = (int) (event.getY() / CELL_SIZE);
                    int col = (int) (event.getX() / CELL_SIZE);
                    ARRAY.setCellStatus(row, col, clickedCellState);
                    ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                } catch (Exception e) {
                    System.out.println("Please drag over cells within the cell area!");
                }
            } else {
                System.out.println("Please enter the edit mode before editing!");
            }
        });

        SCENE.setOnMouseDragReleased(event -> { //Event that does nothing to prevent that the last cell getting dragged over gets changed twice
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (RUNNING && timeStamp % simulationSpeed == 0) {
                    ARRAY.update();
                    timeStamp = 0;
                }

                timeStamp++;
                updateCellApparell();
            }
        }.start();

        for (Node node : ROOT.getChildren()) {
            if (node instanceof Button || node instanceof ToggleButton) {
                node.setFocusTraversable(false);
            }
        }
        ROOT.requestFocus();

        ROOT.addEventHandler(KeyEvent.KEY_PRESSED, event -> { // Event handler for key presses, to control game with keyboard
            if (event.getCode() == KeyCode.E) {
                Edit.fire();
            }
            if (event.getCode() == KeyCode.S) {
                Save.fire();
            }
            if (event.getCode() == KeyCode.N) {
                nextStep.fire();
            }
            if (event.getCode() == KeyCode.SPACE) {
                StartStop.fire();
            }
            event.consume();
        });
    }

    private void updateCellApparell() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private void createCells() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                ROOT.getChildren().add(cell);
            }
        }
    }
}
