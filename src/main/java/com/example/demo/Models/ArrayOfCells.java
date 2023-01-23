package com.example.demo.Models;

public class ArrayOfCells {
    private int[][] Array; //1 = LIFE, 0 = DEAD
    int numberOfColoums, numberOfRows;

    public ArrayOfCells(int numberOfRows, int numberOfColoums) {
        Array = new int[numberOfRows+1][numberOfColoums+1]; //All Cells initialized with 0
        this.numberOfColoums = numberOfColoums;
        this.numberOfRows = numberOfRows;
    }

    public void create(double seed){    //Some Cells become Alife on a random Basis
        for (int y = 0; y <= numberOfRows; y++) {
            for (int x = 0; x <= numberOfColoums; x++) {
                if(Math.random() < seed){
                    Array[y][x] = 1;
                }
            }
        }
    }

    public void update() {
        //Update the Cells according to the rules of the Game
        int[][] copiedArray = Array;
        for (int row = 0; row <= numberOfRows; row++) {
            for (int col = 0; col <= numberOfColoums; col++) {

                int sum = getSum(col,row,copiedArray);

                //A Dead Cells becomes Alive if it has 3 Neighbours
                if(copiedArray[row][col] == 0){
                    if(sum == 3){
                        Array[row][col] = 1;
                    }
                }

                //A living cell dies if it has less than 2 or more than 3 neighbours
                else if(copiedArray[row][col] == 1){
                    if(sum < 2 || sum > 3){
                        Array[row][col] = 0;
                    }
                }
            }
        }
    }

    private int getSum(int x, int y, int[][] copiedArray) {
        //Sum up all the neighbouring cells. Make sure that no ArrayOutOfBounds Exception can come up
        int sumOfAllNeighbours = 0;

        if(x-1 >= 0 && y-1 >= 0){
            sumOfAllNeighbours += copiedArray[y-1][x-1];
        }
        if(x-1 >= 0){
            sumOfAllNeighbours += copiedArray[y][x-1];
        }
        if(x-1 >= 0 && y+1 <= numberOfRows){
            sumOfAllNeighbours += copiedArray[y+1][x-1];
        }
        if(y-1 >= 0){
            sumOfAllNeighbours += copiedArray[y-1][x];
        }
        if(y+1 <= numberOfRows){
            sumOfAllNeighbours += copiedArray[y+1][x];
        }
        if(x+1 <= numberOfColoums && y-1 >= 0){
            sumOfAllNeighbours += copiedArray[y-1][x+1];
        }
        if(x+1 <= numberOfColoums){
            sumOfAllNeighbours += copiedArray[y][x+1];
        }
        if(x+1 <= numberOfColoums && y+1 <= numberOfRows){
            sumOfAllNeighbours += copiedArray[y+1][x+1];
        }
        return sumOfAllNeighbours;
    }

    @Override
    public String toString(){
        String output = "";
        for (int [] row: Array) {
            for (int value: row) {
                output += " " + value + " ";
            }
            output += "\n";
        }
        output += "\n";
        return output;
    }

    public int[][] getArray(){
        return Array;
    }

    public void changeCellStatus(int row, int col){
        if(Array[row][col] == 1) Array[row][col] = 0;
        else Array[row][col] = 1;
    }

    public void setCellStatus(int row, int col, int value){
        Array[row][col] = value;
    }

    public int getNumberOfColoums() {
        return numberOfColoums;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
