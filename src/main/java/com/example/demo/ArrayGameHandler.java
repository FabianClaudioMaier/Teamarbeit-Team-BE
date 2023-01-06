package com.example.demo;

import java.util.Timer;
import java.util.TimerTask;

public class ArrayGameHandler {
    private final Array array;
    private boolean gameStatePlay = false;
    private final Timer timer = new Timer();
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
        if (gameStatePlay) timer.cancel();
        else timer.scheduleAtFixedRate(ttUpdateArray, 0, 500);
        gameStatePlay = !gameStatePlay;
    }
}
