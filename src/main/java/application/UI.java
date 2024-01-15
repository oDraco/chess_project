package application;

import boardgame.Piece;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner scanner) {
        try {
            String pos = scanner.nextLine().toLowerCase();
            int row = Integer.parseInt(pos.substring(1));
            return new ChessPosition(pos.charAt(0), row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Invalid ChessPosition Data. Valid values are between a1-h8.");
        }
    }

    public static void printMatch(ChessMatch match, List<Piece> captured) {
        printBoard(match.getPieces());
        System.out.println("\nTurn: " + match.getTurn());
        if(!match.getCheckMate()) {
            System.out.println("Current Player: " + match.getCurrentPlayer());
            if(match.getCheck()) {
                System.out.println("CHECK!");
            }
        } else {
            System.out.println("Winner: " + match.getCurrentPlayer());
            System.out.println("CHECKMATE!");
        }

        System.out.println();
        printCapturedPieces(captured);
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(pieces.length - i + " ");
            for (int j = 0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(pieces.length - i + " ");
            for (int j = 0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }


    private static void printPiece(ChessPiece piece, boolean hightlightBackground) {
        if (hightlightBackground) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            System.out.print(
                    (piece.getColor() == Color.WHITE ? ANSI_WHITE : ANSI_YELLOW)
                            + piece
                            + ANSI_RESET
            );
        }
        System.out.print(" ");
    }

    private static void printCapturedPieces(List<Piece> pieces) {
        List<Piece> whitePieces = pieces.stream().filter(p -> ((ChessPiece) p).getColor()==Color.WHITE).collect(Collectors.toList());
        List<Piece> blackPieces = pieces.stream().filter(p -> ((ChessPiece) p).getColor()==Color.BLACK).collect(Collectors.toList());

        System.out.println("Captured Pieces:");
        System.out.print("White: ");
        System.out.println(ANSI_WHITE + Arrays.toString(whitePieces.toArray()) + ANSI_RESET);
        System.out.print("Black: ");
        System.out.println(ANSI_YELLOW + Arrays.toString(blackPieces.toArray()) + ANSI_RESET);
    }
}
