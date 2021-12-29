package cs1302.game;

import java.util.Scanner;

/**
 * Driver class that runs the code in {@code cs1302.game.MinesweeperGame}.
 */
public class MinesweeperDriver {

    /**
     * Main method that executes all the code.
     *
     * @param args  String array to hold arguments from the command line
     */
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);

        if (args.length != 1 || args.length == 0) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        }

        String seedPath = args[0];

        MinesweeperGame minesweeper = new MinesweeperGame(stdIn, seedPath);

        minesweeper.play();
    }
}
