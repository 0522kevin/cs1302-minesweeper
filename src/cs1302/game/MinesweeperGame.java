package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

/**
 * Class file that holds the code that runs {@code cs1302.game.MinesweeperDriver}.
 */
public class MinesweeperGame {
    private int row = Integer.MIN_VALUE;
    private int column = Integer.MIN_VALUE;
    private int numMines;
    private int userRound = 0;

    private double userScore;

    private boolean userLife = true;
    private boolean winStatus = false;

    File file;

    Scanner scan;
    Scanner fileScanner;
    Scanner copyScanner;

    private String fullCommand;
    private String sfme1 = "Seed File Malformed Error: Cannot create ";
    private String sfme2 = "a mine field with that many rows and/or columns!";

    private String[][] mineArray;
    private boolean[][] revealArray;
    private boolean[][] copyMineArray;



    /**
     * Constructor for the game. Creates {@link java.io.File} object to read the
     * file passed by the command line. Creates {@link java.util.Scanner} object to
     * stdIn Scanner object in order to parse individual characters. Creates two arrays,
     * mineArray and copyMineArray. mineArray is used to hold markings and will be printed.
     * copyMineArray holds boolean values to indicate whether a mine is stored or not.
     *
     * @param stdIn  Scanner object that reads the command line argument.
     * @param seedPath  String that contains the path to the format of the game.
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        try {
            file = new File(seedPath);
            fileScanner = new Scanner(file);
            copyScanner = stdIn;

            createMap();
        } catch (FileNotFoundException fnfe) {
            String fnfeError1 = "Seedfile Not Found Error: ";
            String fnfeError2 = " (No such file or directory)";

            System.err.println();
            System.err.println(fnfeError1 + seedPath + fnfeError2);
            System.exit(2);
        } // try
    } // MinesweeperGame

    /**
     * Creates the array that will be printed to the screen.
     */
    public void createMap() {
        try {
            row = fileScanner.nextInt();
            if (row == Integer.MIN_VALUE) {
                System.err.println();
                System.err.println("Seed File Malformed Error: Missing Value");
                System.exit(3);
            }
            if ((row < 5) || (10 < row)) {
                throw new NoSuchElementException();
            } // if
            column = fileScanner.nextInt();
            if (column == Integer.MIN_VALUE) {
                System.err.println();
                System.err.println("Seed File Malformed Error: Missing Value");
                System.exit(3);
            }
            if ((column < 5) || (10 < column)) {
                throw new NoSuchElementException();
            } // if
            numMines = fileScanner.nextInt();
            if ((numMines < 1) || (numMines > ((row * column) - 1))) {
                invalidMineCount();
            } // if
            mineArray = new String[row][column];
            copyMineArray = new boolean[row][column];
            revealArray = new boolean[row][column];
            for (int i = 0; i < numMines; i++) {
                int tempMineRow = fileScanner.nextInt();
                int tempMineColumn = fileScanner.nextInt();
                if (isInBounds(tempMineRow, tempMineColumn) == false) {
                    mineNotInBounds();
                } // if
                for (int x = 0; x < row; x++) {
                    for (int y = 0; y < column; y++) {
                        if ((x == tempMineRow) && (y == tempMineColumn)) {
                            copyMineArray[x][y] = true;
                        } // if
                        mineArray[x][y] = "   ";
                    } // for loop for y
                } // for loop for x
            } // for loop for i
        } catch (InputMismatchException ime) {
            System.err.println();
            System.err.println("Seed File Malformed Error: Incorrect Datatype Detected");
            System.exit(3);
        } catch (NoSuchElementException nsee) {
            if ((row < 5) || (10 < row) || (column < 5) || (10 < column)) {
                System.err.println();
                System.err.println(sfme1 + sfme2);
                System.exit(3);
            } else {
                System.err.println();
                System.err.println("Seed File Malformed Error: Missing Value");
                System.exit(3);
            }
        }

    } // createMap

    /**
     * Method for error handling. Triggered when mine is not in bounds.
     * Will exit the program.
     */
    public void mineNotInBounds() {
        System.err.println();
        System.err.println("Seed File Malformed Error: Mine Coordinate Out of Bounds");
        System.exit(3);
    }

    /**
     * Method for error handling. Triggered when mine count is invalid.
     * Will exit the program.
     */
    public void invalidMineCount() {
        System.err.println();
        System.err.println("Seed File Malformed Error: Invalid mine count");
        System.exit(3);
    }

    /**
     * Print the welcome banner to standard output.
     */
    public void printWelcome() {
        String line = " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|";
        System.out.println("        _");
        System.out.println("  /\\/\\ (F)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(line);
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                             ALPHA EDITION |_| v2021.fa");
    } // printWelcome

    /**
     * Print the current contents of the mind field to standard output.
     */
    public void printMineField() {
        System.out.println();
        System.out.println(" Rounds Completed: " + userRound);
        System.out.println();

        for (int i = 0; i <= row; i++) {
            if (i < row) {
                System.out.print(" " + i + " |");
            } else {
                System.out.print("  ");
            } // if
            for (int j = 0; j < column; j++) {
                if (i < row) {
                    System.out.print(mineArray[i][j] + "|");
                } else {
                    System.out.print("   " + j);
                } // if
            } // for loop for j
            System.out.println();
        } // for loop for i
    } // printMineField

    /**
     * Print the game prompt to standard output and interpret user input from standard input.
     */
    public void promptUser() {
        try {
            System.out.print("\nminesweeepr-alpha: ");

            fullCommand = copyScanner.nextLine();
            scan = new Scanner(fullCommand);
            String userCommand = scan.next();

            if (userCommand.equals("h") || userCommand.equals("help")) {
                help();
            } else if (userCommand.equals("r") || userCommand.equals("reveal")) {
                reveal();
            } else if (userCommand.equals("m") || userCommand.equals("mark")) {
                mark();
            } else if (userCommand.equals("g") || userCommand.equals("guess")) {
                guess();
            } else if (userCommand.equals("nofog")) {
                nofog();
            } else if (userCommand.equals("q") || userCommand.equals("quit")) {
                quit();
            } else {
                System.out.println();
                System.out.println("Invalid Command: Command not recognized!");
            }
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command: Command not recognized!");
        }
    }

    /**
     * Triggered when the user types "h" or "help" as the first token.
     * Will print available commands. Uses one round.
     */
    public void help() {
        System.out.println();
        System.out.println("Commands Available...");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" -   Mark: m/mark   row col");
        System.out.println(" -  Guess: g/guess  row col");
        System.out.println(" -   Help: h/help");
        System.out.println(" -   Quit: q/quit");

        userRound++;
        printMineField();
    } // help

    /**
     * Triggered when the user types "r" or "reveal" as the first token.
     * If the space the user wants to reveal is not a mine, this command
     * will reveal the number of adjacent mines. If the space the user
     * wants to reveal is a mine, the game will end.
     */
    public void reveal() {
        try {
            String temp;
            int newRow = scan.nextInt();
            int newColumn = scan.nextInt();
            if (isInBounds(newRow, newColumn) == true) {
                if (copyMineArray[newRow][newColumn] == false) {
                    temp = String.valueOf(getNumAdjMines(newRow, newColumn));
                    mineArray[newRow][newColumn] = (" " +  temp + " ");
                    userRound++;
                    revealArray[newRow][newColumn] = true;
                    printMineField();
                } else {
                    printLoss();
                } // if to check whether the space the user wants to reveal has a mine or not
            } // if
            if (isRowInBounds(newRow) == false) {
                String errorMessage1 = "Invalid Command: Index ";
                String errorMessage2 = " out of bounds for length ";
                System.out.println();
                System.out.println(errorMessage1 + newRow + errorMessage2 + row);
                printMineField();
            } else if (isColumnInBounds(newColumn) == false) {
                String errorMessage1 = "Invalid Command: Index ";
                String errorMessage2 = " out of bounds for length ";
                System.out.println();
                System.out.println(errorMessage1 + newColumn + errorMessage2 + column);
                printMineField();
            } // if for column out of bounds
        } catch (InputMismatchException anotherIME) {
            System.err.println();
            System.err.println("Invalid Command: null");
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command: null");
        }
    } // reveal

    /**
     * Triggered when the user types "m" or "mark" as the first token.
     * Will mark the space " F " no matter what it used to hold.
     */
    public void mark() {
        try {
            int tempRow = scan.nextInt();
            int tempColumn = scan.nextInt();

            if (isInBounds(tempRow, tempColumn) == true) {
                mineArray[tempRow][tempColumn] = " F ";
                userRound++;
                printMineField();
            } else {
                if (isRowInBounds(tempRow) == false) {
                    String errorMessage1 = "Invalid Command: Index ";
                    String errorMessage2 = " out of bounds for length ";
                    System.out.println();
                    System.out.println(errorMessage1 + tempRow + errorMessage2 + row);
                    printMineField();
                } // if for row out of bounds

                if (isColumnInBounds(tempColumn) == false) {
                    String errorMessage1 = "Invalid Command: Index ";
                    String errorMessage2 = " out of bounds for length ";
                    System.out.println();
                    System.out.println(errorMessage1 + tempColumn + errorMessage2 + column);
                    printMineField();
                } // if for column out of bounds
            } // if
        } catch (InputMismatchException anotherIME) {
            System.err.println();
            System.err.println("Invalid Command: null");
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command: null");
        }
    } // mark

    /**
     * Triggered when the user types "g" or "guess" as the first token.
     * Will mark the space " ? " no matter what it used to hold.
     */
    public void guess() {
        try {
            int tempRow = scan.nextInt();
            int tempColumn = scan.nextInt();

            if (isInBounds(tempRow, tempColumn) == true) {
                mineArray[tempRow][tempColumn] = " ? ";
                userRound++;
                printMineField();
            } else {
                if (isRowInBounds(tempRow) == false) {
                    String errorMessage1 = "Invalid Command: Index ";
                    String errorMessage2 = " out of bounds for length ";
                    System.out.println();
                    System.out.println(errorMessage1 + tempColumn + errorMessage2 + column);
                    printMineField();
                } // if for row out of bounds

                if (isColumnInBounds(tempColumn) == false) {
                    String errorMessage1 = "Invalid Command: Index ";
                    String errorMessage2 = " out of bounds for length ";
                    System.out.println();
                    System.out.println(errorMessage1 + tempColumn + errorMessage2 + column);
                    printMineField();
                } // if for column out of bounds
            } // if
        } catch (InputMismatchException anotherIME) {
            System.err.println();
            System.err.println("Invalid Command: null");
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command: null");
        }
    } // guess

    /**
     * Triggered when the user types "nofog" as the first token.
     * Will reveal the mines' locations using "<" and ">" characters.
     * Whatever information that was printed before will be inside the
     * braces.
     */
    public void nofog() {
        String[][] anotherCopy = new String[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (copyMineArray[i][j] == false) {
                    anotherCopy[i][j] = mineArray[i][j];
                } else {
                    anotherCopy[i][j] = "<" + mineArray[i][j].substring(1,2) + ">";
                } // if
            } // for loop for j
        } // for loop for i

        userRound++;

        System.out.println();
        System.out.println(" Rounds Completed: " + userRound);
        System.out.println();

        for (int i = 0; i <= row; i++) {
            if (i < row) {
                System.out.print(" " + i + " |");
            } else {
                System.out.print("  ");
            } // if
            for (int j = 0; j < column; j++) {
                if (i < row) {
                    System.out.print(anotherCopy[i][j] + "|");
                } else {
                    System.out.print("   " + j);
                } // if
            } // for loop for j
            System.out.println();
        } // for loop for i
    } // nofog

    /**
     * Triggered when the user types "q" or "quit" as the first token.
     * Will gracefully exit the game.
     */
    public void quit() {
        System.out.println();
        System.out.println("Quitting the game...");
        System.out.println("Bye!");
        System.exit(0);
    } // quit

    /**
     * Returning variable indicates whether the user is alive or not.
     *
     * @return userLife  boolean that holds qwhether the user is alive or not.
     */
    public boolean isAlive() {
        return userLife;
    } // isAlive

    /**
     * This method returns true if all conditions are met to win.
     *
     * @return winStatus  Holds the status of whether the user has won the game or not.
     */
    public boolean isWon() {
        int numRevealMines = 0;
        int spaceToReveal = (row * column) - numMines;

        for (int x = 0; x < row; x++) {
            for (int y = 0; y < row; y++) {
                if ((copyMineArray[x][y] == true) && (mineArray[x][y].equals(" F "))) {
                    numRevealMines++;
                } // if
                if ((copyMineArray[x][y] == false) && (revealArray[x][y] == true)) {
                    spaceToReveal--;
                } // if
            } // for loop for y
        } // for loop for x

        if ((numRevealMines == numMines) && (spaceToReveal == 0)) {
            winStatus = true;
        }

        return winStatus;
    } // isWon

    /**
     * print the win message to standard output.
     */
    public void printWin() {
        userScore = 100.0000 * (row * column) / userRound;
        String scoreString = String.format("%.2f", userScore);

        System.out.println();
        System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
        System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
        System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
        System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
        System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
        System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
        System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
        System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
        System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
        System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
        System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
        System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
        System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
        System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
        System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
        System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
        System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + scoreString);
    } // printWin

    /**
     * Print the game over message to standard output.
     */
    public void printLoss() {
        System.out.println();
        System.out.println(" Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/");
        System.out.println();
        System.exit(0);
    } // printLoss

    /**
     * Provide the main game loop by invoking other instance methods as needed.
     */
    public void play() {
        printWelcome();
        printMineField();

        while (isAlive()) {
            promptUser();

            if (isWon() == true) {
                break;
            }
        } // while

        if (isWon() == true) {
            printWin();
        } // if
    } // play

    /**
     * Counts the number of mines that are adjacent to the space user wants to reveal.
     *
     * @param userRow  the row on the grid the user wants to reveal.
     * @param userColumn  the column on the grid the user wants to reveal.
     * @return adjMines  the number of adjacent mines.
     */
    private int getNumAdjMines(int userRow, int userColumn) {
        int adjMines = 0;

        for (int i = userRow - 1; i < userRow + 2; i++) {
            for (int j = userColumn - 1; j < userColumn + 2; j++) {
                if (isInBounds(i, j) == true) {
                    if (copyMineArray[i][j] == true) {
                        adjMines++;
                    } // if to check whether mine exists or not
                } // if to check whether the square is in bounds or not
            } // for loop for j
        } // for loop for i

        return adjMines;
    } // getNumAdjMines

    /**
     * Checks whether the space user wants to reveal is within the grid.
     *
     * @param userRow  row on the grid the user wants to reveal.
     * @param userColumn  column on the grid the user wants to reveal.
     * @return inside  holds the information about whether the space is within the grid.
     */
    private boolean isInBounds(int userRow, int userColumn) {
        boolean inside = false;

        if ((0 <= userRow) && (userRow <= row - 1)) {
            if ((0 <= userColumn) && (userColumn <= column - 1)) {
                inside = true;
            } else {
                inside = false;
            } // if to check whether the column is in bounds or not
        } else {
            inside = false;
        } // if to check whether the row is in bounds or not

        return inside;
    } // isInBounds for both row and column

    /**
     * Checks whether the row of the space
     * the user wants an action is within the grid or not.
     *
     * @param userRow  the row on the grid the user wants an action.
     * @return answer  holds the answer whether the row is on the grid or not.
     */
    private boolean isRowInBounds(int userRow) {
        boolean answer;

        if ((0 <= userRow) && (userRow <= row - 1)) {
            answer = true;
        } else {
            answer = false;
        } // if

        return answer;
    } // isRowInBounds

    /**
     * Checks whether the column of the space the user wants an action is within the grid.
     *
     * @param userColumn  the column on the grid the user wants an action.
     * @return answer  holds the answer whether the column is on the grid or not.
     */
    private boolean isColumnInBounds(int userColumn) {
        boolean answer;

        if ((0 <= userColumn) && (userColumn <= column - 1)) {
            answer = true;
        } else {
            answer = false;
        } // if

        return answer;
    } // isColumnInBounds
}
