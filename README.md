# cs1302-minesweeper

This repository contains a Java implementation of the classic game Minesweeper. The game can be played in the console or in a GUI using JavaFX.

Requirements

To build and run the game, you will need to have the following software installed on your system:

Java SE Development Kit 8 or later
Apache Maven
Building and Running the Game

To build the game, navigate to the root directory of the repository and run the following command:

Copy code
mvn package
This will compile the Java source code and create a JAR file in the target directory.

To run the game in the console, use the following command:

Copy code
java -cp target/cs1302-minesweeper-1.0.0.jar edu.gatech.cs1302.minesweeper.console.Console
To run the game in the GUI, use the following command:

Copy code
java -cp target/cs1302-minesweeper-1.0.0.jar edu.gatech.cs1302.minesweeper.gui.Minesweeper
Game Rules

The objective of Minesweeper is to clear the board of all mines without detonating any of them. The board is divided into cells, some of which contain mines. You can reveal a cell by clicking on it. If the cell contains a mine, the game is over. If the cell does not contain a mine, it will display a number indicating the number of mines in the surrounding cells. Use this information to deduce where the mines are and flag them with right-click. The game is won when all mines have been flagged.

Contributions

This repository is intended for educational purposes only. As such, pull requests will not be accepted. If you have suggestions for improvements or corrections, please open an issue.
