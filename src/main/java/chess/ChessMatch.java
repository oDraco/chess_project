package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board;
    private Boolean check;
    private Boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public ChessMatch() {
        board = new Board(8,8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i=0; i < pieces.length; i++) {
            for(int j=0; j< pieces[i].length; j++) {
                pieces[i][j] = (ChessPiece) board.getPiece(i,j);
            }
        }
        return pieces;
    }

    private void initialSetup() {
        board.placePiece(new King(board,Color.BLACK),new Position(0,4));
        board.placePiece(new King(board,Color.WHITE),new Position(7,4));
    }
}
