package application;

import boardgame.Board;
import chess.ChessMatch;

import java.util.Locale;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ChessMatch match = new ChessMatch();
        UI.printBoard(match.getPieces());
    }
}
