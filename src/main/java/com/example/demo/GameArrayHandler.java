package com.example.demo;

import com.example.demo.Models.Array;

import java.util.Timer;
import java.util.TimerTask;

public class GameArrayHandler { // GameArrayHandler provides easy access between frontend and the game array
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

    public GameArrayHandler(Array array) {
        this.array = array;
    }

    public void playPause() { // Provides functionality for the play/pause button in the frontend
        if (gameStatePlay) {
            timer.cancel();
            timer = null;
        } else {
            timer = new Timer();
            timer.scheduleAtFixedRate(ttUpdateArray, 0, 500);
        }
        gameStatePlay = !gameStatePlay;
    }

    public void nextStep() { // Provides functionality for the next step button in the frontend
        array.update();
        System.out.println(array);
    }

    public void changeCellStatus(int row, int col) { // Provides functionality to change the status of cells which got clicked
        array.changeCellStatus(row, col);
        System.out.println(array);
    }
}