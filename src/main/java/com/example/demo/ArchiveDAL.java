package com.example.demo;

import com.example.demo.Models.ArrayOfCells;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public final class ArchiveDAL { // Provides functionality of saving configurations to CSV files
    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void save(ArrayOfCells arrayOfCells) { // Saves the current config as a CSV file within the Archive directory
        LocalDateTime now = LocalDateTime.now();
        File arrayCSV = new File("src/main/Archive/" + dtf.format(now) + ".csv");

        List<String> dataLines = new ArrayList<>();
        for (int i = 0; i < arrayOfCells.getNumberOfRows(); i++) {
            StringBuilder dataLine = new StringBuilder();
            for (int j = 0; j < arrayOfCells.getNumberOfColoums(); j++) {
                dataLine.append(arrayOfCells.getArray()[i][j]);
                if (j < arrayOfCells.getNumberOfColoums() - 1) dataLine.append(",");
            }
            dataLines.add(dataLine.toString());
        }

        try {
            PrintWriter pw = new PrintWriter(arrayCSV);
            for (String dataLine : dataLines) {
                pw.println(dataLine);
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            errorHandling(e, "Beim Speichern ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.", "Fehler beim Speicherversuch");
        }
    }

    public static ArrayOfCells load(File fileToLoad) { // loads the passed file (passed from Archive view) into a new array and returns that, if an error occurs, null gets returned
        ArrayOfCells arrayOfCells;
        List<String[]> rows = new ArrayList<>();
        String dataRow;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileToLoad));
            while ((dataRow = csvReader.readLine()) != null) {
                String[] row = dataRow.split(",");
                rows.add(row);
            }

            arrayOfCells = new ArrayOfCells(rows.size(), rows.get(1).length);
            for (int i = 0; i < arrayOfCells.getNumberOfRows(); i++) {
                for (int j = 0; j < arrayOfCells.getNumberOfColoums(); j++) {
                    arrayOfCells.setCellStatus(i, j, Integer.parseInt(rows.get(i)[j]));
                }
            }

        } catch (Exception e) {
            errorHandling(e, "Beim Laden der Konfiguration ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.", "Fehler beim Ladeversuch");
            return null;
        }
        return arrayOfCells;
    }

    private static void errorHandling(Exception e, String message, String title) { // Method to print error details to the console and inform the user via a message box
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
