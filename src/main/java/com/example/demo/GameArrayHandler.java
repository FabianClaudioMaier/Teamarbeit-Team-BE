package com.example.demo;

import com.example.demo.Models.ArrayOfCells;

import java.util.Timer;
import java.util.TimerTask;

public class GameArrayHandler { // GameArrayHandler provides easy access between frontend and the game array
    private final ArrayOfCells arrayOfCells;
    private boolean gameStatePlay = false;

    public GameArrayHandler(ArrayOfCells arrayOfCells) {
        this.arrayOfCells = arrayOfCells;
    }

    public void nextStep() { // Provides functionality for the next step button in the frontend
        arrayOfCells.update();
    }

    public void changeCellStatus(int row, int col) {
        arrayOfCells.changeCellStatus(row, col);
    }
    
    public void setCellStatus(int row, int col, int value) {
        arrayOfCells.setCellStatus(row, col, value);
    }

    public void clearArray() {
        for (int i = 0; i < arrayOfCells.getNumberOfRows(); i++) {
            for (int j = 0; j < arrayOfCells.getNumberOfColoums(); j++) {
                arrayOfCells.setCellStatus(i,j,0);
            }
        }
    }
}
