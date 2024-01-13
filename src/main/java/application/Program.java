package application;

import boardgame.Board;

import java.util.Locale;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Board board = new Board(8,8);
    }
}
