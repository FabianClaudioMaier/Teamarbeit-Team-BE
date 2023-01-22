package com.example.demo;

import com.example.demo.Models.Array;
//import com.example.demo.Models.Coordinates;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
/*<<<<<<< HEAD
import javafx.scene.input.MouseEvent;
=======*/
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//>>>>>>> laurin-new
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static List<File> SAVES = new ArrayList<>();

    private static GameArrayHandler gameArrayHandler = new GameArrayHandler(ARRAY);

    private static int currentFileInArchive = 0;


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
                gameArrayHandler.nextStep();
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
                gameArrayHandler.playPause();
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
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                currentFileInArchive = 0;
                ARRAY = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                updateCellApparell();
            }
        });

        Button Next = new Button("Next");
        Next.setLayoutX(WIDTH + 10);
        Next.setLayoutY(300);

        Next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                if(currentFileInArchive + 1 < filesInArchive.length) {
                    currentFileInArchive++;
                    ARRAY = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                    updateCellApparell();
                }
            }
        });

        Button Previous = new Button("Previous");
        Previous.setLayoutX(WIDTH + 10);
        Previous.setLayoutY(350);

        Previous.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                if(currentFileInArchive - 1 >= 0) {
                    currentFileInArchive--;
                    ARRAY = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                    updateCellApparell();
                }
            }
        });

        createCells();

        ROOT.getChildren().add(nextStep);
        ROOT.getChildren().add(StartStop);
        ROOT.getChildren().add(Edit);
        ROOT.getChildren().add(Save);
        ROOT.getChildren().add(GoToArchive);
        ROOT.getChildren().add(Next);
        ROOT.getChildren().add(Previous);

        primaryStage.setScene(SCENE);
        primaryStage.show();


        SCENE.setOnMouseClicked( event -> {
            if(EDITING) {
                int row = (int) (event.getY() / CELL_SIZE);
                int col = (int) (event.getX() / CELL_SIZE);
                gameArrayHandler.changeCellStatus(row,col);
                ((Rectangle) ROOT.getChildren().get(row * COLUMNS + col)).setFill(ARRAY.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);

            } else {
                System.out.println("please Stop the Game to change states");
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if(RUNNING && timeStamp%simulationSpeed == 0){
                    gameArrayHandler.nextStep();
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

    //Listing the Files is Archive, source: https://www.baeldung.com/java-list-directory-files
    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
