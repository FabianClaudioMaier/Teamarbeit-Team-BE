package com.example.demo;

import com.example.demo.Models.Array;
import com.example.demo.Models.Coordinates;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ArrayGameHandler {
    private final Array array;
    private boolean gameStatePlay = false;
    private Timer timer;

    private final TimerTask ttUpdateArray = new TimerTask() {
        @Override
        public void run() {
            array.update();
            System.out.println(array);
        }
    };

    public ArrayGameHandler(Array array) {
        this.array = array;
    }

    public void playPause() {
        if (gameStatePlay) {
            timer.cancel();
            timer = null;
        } else {
            timer = new Timer();
            timer.scheduleAtFixedRate(ttUpdateArray, 0, 500);
        }
        gameStatePlay = !gameStatePlay;
    }

    public void nextStep() {
        array.update();
        System.out.println(array);
    }

    public void changeCellStatus(int row, int col) {
        array.setArray(row, col);
        System.out.println(array);
    }

    public void changeHoverCellsStatus(List<Coordinates> listOfCoordinates) {
        for (Coordinates coordinate : listOfCoordinates) {
            changeCellStatus(coordinate.getxCoordinate(), coordinate.getyCoordinate());
        }
    }
}