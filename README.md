# Teamarbeit-Team-BE
CSDC-BB Group 2 Team BE. 

## Teammembers:
 * Lilli Jahn 
 * Laurin Knünz 
 * Vincent Lim 
 * Fabian Maier

## Project:
John Conways Game of Life is a simple set of rules working on cells in a grid. The cell can be either dead (white) or alive (black). 
With each iteration it changes it status according to a simple set of rules. (source: http://pi.math.cornell.edu/~lipa/mec/lesson6.html)
 * If the cell is alive, then it stays alive if it has either 2 or 3 live neighbors
 * If the cell is dead, then it springs to life only in the case that it has 3 live neighbors

## The Project should include the following functions:
 * load and save configurations (x)
 * simulation speed configurable ( )
 * manuell steps (x)
 * Pause / Next Step / Play (x)
 * Editor (x)
 * world size configurable ( )
 * Think about possible extensions you could implement (5 member group)
 
## How to play
 * start the application by running the GameOfLife.main() method
 * U can now see a screen with white and black Cells and a number of Buttons
 * Run lets the simulation progress every 10 seconds
 * Stop halts it
 * Press Start Editing to beginn changeing the state of the boxes
 * Save puts the current setting into a List
 * Go to to Archive retrieves the last list object that was saved
