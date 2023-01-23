package ac.at.fhcampuswien;

import ac.at.fhcampuswien.Models.ArrayOfCells;

import java.util.Timer;
import java.util.TimerTask;

public class GameArrayHandler { // GameArrayHandler provides easy access between frontend and the game array
    private final ArrayOfCells arrayOfCells;
    private boolean gameStatePlay = false;
    private Timer timer;

    private final TimerTask ttUpdateArray = new TimerTask() {
        @Override
        public void run() {
            arrayOfCells.update();
        }
    };

    public GameArrayHandler(ArrayOfCells arrayOfCells) {
        this.arrayOfCells = arrayOfCells;
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
        arrayOfCells.update();
    }

    public void changeCellStatus(int row, int col) {
        arrayOfCells.changeCellStatus(row, col);
    }

    public void clearArray() {
        for (int i = 0; i < arrayOfCells.getNumberOfRows(); i++) {
            for (int j = 0; j < arrayOfCells.getNumberOfColoums(); j++) {
                arrayOfCells.setCellStatus(i,j,0);
            }
        }
    }
}