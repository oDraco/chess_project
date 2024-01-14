package chess;

import boardgame.Board;
import boardgame.Piece;
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

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column,row).toPosition());
    }

    public ChessPiece performChessMove(ChessPosition initialPosition, ChessPosition targetPosition) {
        Position initialPos = initialPosition.toPosition();
        Position targetPos = targetPosition.toPosition();
        validateOriginPosition(initialPos);
        validateTargetPosition(initialPos,targetPos);
        Piece capturedPiece = makeMove(initialPos,targetPos);
        return (ChessPiece) capturedPiece;
    }

    private void validateOriginPosition(Position position) {
        if(!board.thereIsAPiece(position)) {
            throw new ChessException("There isn't a piece in the selected location");
        }
        if(!board.getPiece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There isn't any possible movement for the selected piece");
        }
    }

    private void validateTargetPosition(Position origin, Position target) {
        if (!board.getPiece(origin).getPossibleMove(target)) {
            throw new ChessException("The chosen piece can't move to the target destination.");
        };
    }

    private Piece makeMove(Position origin, Position target) {
        Piece p = board.removePiece(origin);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p,target);
        return capturedPiece;
    }

    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }


}
