package com.example.demo.Models;

public class Array {
    private int[][] Array; //1 = LIFE, 0 = DEAD
    int numberOfColoums, numberOfRows;

    public Array(int numberOfRows, int numberOfColoums) {
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
        int[][] copiedArray = Array;
        for (int y = 0; y <= numberOfRows; y++) {
            for (int x = 0; x <= numberOfColoums; x++) {
                int sum = getSum(x,y,copiedArray);
                if(copiedArray[y][x] == 0){
                    if(sum == 3){
                        Array[y][x] = 1;
                    }
                }
                else if(copiedArray[y][x] == 1){
                    if(sum < 2){
                        Array[y][x] = 0;
                    }
                    else if(sum > 3){
                        Array[y][x] = 0;
                    }
                }
            }
        }
    }

    private int getSum(int x, int y, int[][] copiedArray) {
        int sum = 0;
        if(x-1 >= 0 && y-1 >= 0){
            sum+=copiedArray[y-1][x-1];
        }
        if(x-1 >= 0){
            sum+=copiedArray[y][x-1];
        }
        if(x-1 >= 0 && y+1 <= numberOfRows){
            sum+=copiedArray[y+1][x-1];
        }
        if(y-1 >= 0){
            sum+=copiedArray[y-1][x];
        }
        if(y+1 <= numberOfRows){
            sum+=copiedArray[y+1][x];
        }
        if(x+1 <= numberOfColoums && y-1 >= 0){
            sum+=copiedArray[y-1][x+1];
        }
        if(x+1 <= numberOfColoums){
            sum+=copiedArray[y][x+1];
        }
        if(x+1 <= numberOfColoums && y+1 <= numberOfRows){
            sum+=copiedArray[y+1][x+1];
        }
        return sum;
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

    public void setArray(int row, int col){
        if(Array[row][col] == 1) Array[row][col] = 0;
        else Array[row][col] = 1;
    }
}
