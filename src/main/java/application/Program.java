package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        ChessMatch match = new ChessMatch();

        while(!match.getCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(match, match.getCapturedPieces());

                System.out.print("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                UI.clearScreen();
                UI.printBoard(match.getPieces(),match.getPossibleMoves(source));

                System.out.print("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);

                match.performChessMove(source, target);
            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(match, match.getCapturedPieces());
    }
}
