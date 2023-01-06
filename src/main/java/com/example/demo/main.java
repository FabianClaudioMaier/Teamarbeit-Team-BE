package com.example.demo;

import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) throws InterruptedException {
        final int NUMBEROFCOLOUMS = 10;
        final int NUMBEROFROWS = 10;
        final double SEED = 0.3;

        Array array = new Array(NUMBEROFROWS, NUMBEROFCOLOUMS);
        array.create(SEED);

        ArrayGameHandler agh = new ArrayGameHandler(array);
        System.out.println("Start in fünf Sekunden!");
        TimeUnit.SECONDS.sleep(5);
        agh.playPause();
        TimeUnit.SECONDS.sleep(2);
        agh.playPause();
        System.out.println("Spiel wurde pausiert nach 2 Sekunden!");
    }
}
