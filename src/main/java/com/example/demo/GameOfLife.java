package com.example.demo;

import com.example.demo.Models.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.stage.WindowEvent;
import java.io.File;

/* Main class*/
public class GameOfLife extends Application {
    private static final int width = 800; //width of the simulation area
    private static final int height = 600; //height of the simulation area
    private static final int rows = 15; //number of rows in the simulation area
    private static final int columns = 20; //number of columns in the simulation area
    private static final int cellSize = 40; //pixel size of one Cell
    private boolean gameStateIsRunning = false;
    private boolean gameStateIsEditing = false;
    private Group SimulationScreenGroup = new Group(); //Group for Screen One containing the Simulation Area
    private Group ArchiveScreenGroup = new Group(); //Group for Screnn Two containing the Archive
    private Scene SimulationScreenScene = new Scene(SimulationScreenGroup, width + 150, height + 100); //Scene for Screen One
    private Scene ArchiveScreenScene = new Scene(ArchiveScreenGroup,width + 150, height + 100); //Scene for Screen Two
    private Scene currentScene = SimulationScreenScene; //controls the State of the Game
    private Group currentGroup = SimulationScreenGroup; //controls the State of the Game
    private static ArrayOfCells backgroundCellArray = new ArrayOfCells(rows, columns); //Array which is getting displayed
    private static int timeStamp = 0; //controls simulation-display rate
    private static int simulationSpeed = 20; //controls the simulation speed
    private static GameArrayHandler gameArrayHandler = new GameArrayHandler(backgroundCellArray); //Connection to the ArrayOfCells
    private static int currentFileInArchive = 0; //Controls which Array is being displayed


    public static void main(String[] args) {
        backgroundCellArray.create(0.5); //seed for the CellArray
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Button nextStep = new Button("next Step");
        nextStep.setLayoutX(width + 10);
        nextStep.setLayoutY(50);

        nextStep.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                gameArrayHandler.nextStep();
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < columns; col++) {
                        ((Rectangle) SimulationScreenGroup.getChildren().get(row * columns + col)).setFill(backgroundCellArray.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                    }
                }
            }
        });

        ToggleButton StartStop = new ToggleButton("Start");
        StartStop.setLayoutX(width + 10);
        StartStop.setLayoutY(100);

        StartStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!gameStateIsEditing) {
                    if (gameStateIsRunning) StartStop.setText("Start");
                    else StartStop.setText("Stop");

                    gameStateIsRunning = !gameStateIsRunning;
                    gameArrayHandler.playPause();
                } else System.out.println("Exit Editing Mode");
            }
        });

        Button Edit = new Button("Start Editing");
        Edit.setLayoutX(width + 10);
        Edit.setLayoutY(150);

        Edit.setOnAction(new EventHandler<ActionEvent>() {
            //When clicked the State of the Game is switched to Editing
            @Override
            public void handle(ActionEvent event) {
                if (!gameStateIsEditing) {
                    if(gameStateIsRunning) gameArrayHandler.playPause();
                    gameStateIsRunning = false;
                    StartStop.setText("Start");
                    Edit.setText("Stop Editing");
                }
                if (gameStateIsEditing) {
                    Edit.setText("Start Editing");
                }
                gameStateIsEditing = !gameStateIsEditing;
            }
        });

        Button Save = new Button("Save");
        Save.setLayoutX(width + 10);
        Save.setLayoutY(200);

        Save.setOnAction(new EventHandler<ActionEvent>() {
            //Save the current Displayed Array to the Archive folder
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save Button clicked");
                if (gameStateIsEditing) {
                    System.out.println("Saved");
                    ArchiveDAL.save(backgroundCellArray);
                } else {
                    System.out.println("you have to be in edit mode to save");
                }
            }
        });

        Button GoToArchive = new Button("Go to Archive");
        GoToArchive.setLayoutX(width + 10);
        GoToArchive.setLayoutY(250);

        GoToArchive.setOnAction(new EventHandler<ActionEvent>() {
            //Change the Scene to Archive
            @Override
            public void handle(ActionEvent event) {
                System.out.println("pressed go to Archive");
                currentScene = ArchiveScreenScene;
                currentGroup = ArchiveScreenGroup;
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                currentFileInArchive = 0;
                backgroundCellArray = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                updateCellApparell(currentGroup);
                primaryStage.setScene(currentScene);
            }
        });

        Button GoToSimulation = new Button("Go to Simulation");
        GoToSimulation.setLayoutX(width + 10);
        GoToSimulation.setLayoutY(50);

        GoToSimulation.setOnAction(new EventHandler<ActionEvent>() {
            //Change the scene from Archive to Simulation
            @Override
            public void handle(ActionEvent event) {
                currentScene = SimulationScreenScene;
                currentGroup = SimulationScreenGroup;
                updateCellApparell(currentGroup);
                primaryStage.setScene(currentScene);
            }
        });

        Button Next = new Button("Next");
        Next.setLayoutX(width + 10);
        Next.setLayoutY(100);

        Next.setOnAction(new EventHandler<ActionEvent>() {
            //Get a List of all Files in the Archive Folder and increase the currentFileInArchive counter
            @Override
            public void handle(ActionEvent event) {
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                if(currentFileInArchive + 1 < filesInArchive.length) {
                    currentFileInArchive++;
                    backgroundCellArray = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                    updateCellApparell(currentGroup);
                }
            }
        });

        Button Previous = new Button("Previous");
        Previous.setLayoutX(width + 10);
        Previous.setLayoutY(150);

        Previous.setOnAction(new EventHandler<ActionEvent>() {
            //Get a List of all Files in the Archive Folder and decrease the currentFileInArchive counter
            @Override
            public void handle(ActionEvent event) {
                File[] filesInArchive = new File("src/main/Archive/").listFiles();
                if(currentFileInArchive - 1 >= 0) {
                    currentFileInArchive--;
                    backgroundCellArray = ArchiveDAL.load(filesInArchive[currentFileInArchive]);
                    updateCellApparell(currentGroup);
                }
            }
        });

        //sets all cells in the Array to 0
        Button Clear = new Button("Clear");
        Clear.setLayoutX(width + 10);
        Clear.setLayoutY(300);

        Clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameArrayHandler.clearArray();
            }
        });

        //create the Rectangles in the Screens
        createCells(ArchiveScreenGroup);
        createCells(SimulationScreenGroup);

        //Add the Buttons to the acccording Screens
        SimulationScreenGroup.getChildren().add(nextStep);
        SimulationScreenGroup.getChildren().add(StartStop);
        SimulationScreenGroup.getChildren().add(Edit);
        SimulationScreenGroup.getChildren().add(Save);
        SimulationScreenGroup.getChildren().add(GoToArchive);
        SimulationScreenGroup.getChildren().add(Clear);

        ArchiveScreenGroup.getChildren().add(Next);
        ArchiveScreenGroup.getChildren().add(Previous);
        ArchiveScreenGroup.getChildren().add(GoToSimulation);

        //Set the Scene to the Stage
        primaryStage.setScene(currentScene);
        //Display the Show
        primaryStage.show();


        //source: https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
        //Close the window
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });


        SimulationScreenScene.setOnMouseClicked(event -> {
            //When in Editing mode, clicking cells changes their state.
            if(gameStateIsEditing) {
                int row = (int) (event.getY() / cellSize);
                int col = (int) (event.getX() / cellSize);
                gameArrayHandler.changeCellStatus(row,col);
                ((Rectangle) SimulationScreenGroup.getChildren().get(row * columns + col)).setFill(backgroundCellArray.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            } else {
                System.out.println("please Stop the Game to change states");
            }
        });



        new AnimationTimer() {
            //Updates cells and display when state is set to running
            @Override
            public void handle(long now) {
                if(gameStateIsRunning && timeStamp%simulationSpeed == 0){
                    gameArrayHandler.nextStep();
                    timeStamp = 0;
                }
                timeStamp++;
                updateCellApparell(currentGroup);
            }
        }.start();

        for (Node node : SimulationScreenGroup.getChildren()) {
            if (node instanceof Button || node instanceof ToggleButton) {
                node.setFocusTraversable(false);
            }
        }
        SimulationScreenGroup.requestFocus();

        SimulationScreenGroup.addEventHandler(KeyEvent.KEY_PRESSED, event -> { // Event handler for key presses, to control game with keyboard
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

    private void updateCellApparell(Group currentGroup) {
        //changes the color of the cells acording to the current state of the array
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                ((Rectangle) currentGroup.getChildren().get(row * columns + col)).setFill(backgroundCellArray.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private void createCells(Group currentGroup) {
        //creates a rectangle for every cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Rectangle cell = new Rectangle(col * cellSize, row * cellSize, cellSize, cellSize);
                cell.setFill(backgroundCellArray.getArray()[row][col] == 1 ? Color.BLACK : Color.WHITE);
                currentGroup.getChildren().add(cell);
            }
        }
    }
}
