package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;

    private final List<Piece> piecesOnTheBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public ChessMatch() {
        board = new Board(8, 8);
        currentPlayer = Color.WHITE;
        turn = 1;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                pieces[i][j] = (ChessPiece) board.getPiece(i, j);
            }
        }
        return pieces;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    private Color getOpponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece getKing(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();
        for (Piece p : list) {
            if (p instanceof King) return (ChessPiece) p;
        }
        throw new IllegalStateException("There isn't a King of color " + color + " on the board.");
    }

    public boolean testCheck(Color color) {
        Position kingPos = getKing(color).getChessPosition().toPosition();
        List<Piece> opponents = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == getOpponent(color)).toList();
        for (Piece p : opponents) {
            boolean[][] moves = p.getPossibleMoves();
            if (moves[kingPos.getRow()][kingPos.getColumn()]) return true;
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) return false;
        List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();
        for (Piece p : pieces) {
            boolean[][] moves = p.getPossibleMoves();
            for (int i = 0; i < moves.length; i++) {
                for (int j = 0; j < moves[i].length; j++) {
                    if (moves[i][j]) {
                        Position origin = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece captured = makeMove(origin, target);
                        boolean isCheck = testCheck(color);
                        undoMove(origin, target, captured);
                        if (!isCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean[][] getPossibleMoves(ChessPosition position) {
        Position p = position.toPosition();
        validateOriginPosition(p);
        return board.getPiece(p).getPossibleMoves();
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public void performChessMove(ChessPosition initialPosition, ChessPosition targetPosition) {
        Position initialPos = initialPosition.toPosition();
        Position targetPos = targetPosition.toPosition();

        validateOriginPosition(initialPos);
        validateTargetPosition(initialPos, targetPos);

        Piece capturedPiece = makeMove(initialPos, targetPos);

        if (testCheck(currentPlayer)) {
            undoMove(initialPos, targetPos, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testCheck(getOpponent(currentPlayer));

        if (testCheckMate(getOpponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // Special Move: En Passant
        ChessPiece movedPiece = (ChessPiece) board.getPiece(targetPos);
        if (movedPiece instanceof Pawn && (targetPos.getRow() == initialPos.getRow() - 2 || targetPos.getRow() == initialPos.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        // return (ChessPiece) capturedPiece;
    }

    private void validateOriginPosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There isn't a piece in the selected location");
        }
        if (((ChessPiece) board.getPiece(position)).getColor() != currentPlayer) {
            throw new ChessException("The selected piece isn't yours");
        }
        if (!board.getPiece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There isn't any possible movement for the selected piece");
        }

    }

    private void validateTargetPosition(Position origin, Position target) {
        if (!board.getPiece(origin).getPossibleMove(target)) {
            throw new ChessException("The chosen piece can't move to the target destination.");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position origin, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(origin);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        p.increaseMoveCount();

        // Special Move: Castling
        // King Side Castling
        if (p instanceof King && target.getColumn() == origin.getColumn() + 2) {
            Position rookSource = new Position(origin.getRow(), origin.getColumn() + 3);
            Position rookTarget = new Position(target.getRow(), target.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }

        // Queen Side Castling
        if (p instanceof King && target.getColumn() == origin.getColumn() - 2) {
            Position rookSource = new Position(origin.getRow(), origin.getColumn() - 4);
            Position rookTarget = new Position(target.getRow(), target.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }

        // Special Move: En Passant
        if (p instanceof Pawn && capturedPiece == null && origin.getColumn() != target.getColumn()) {
            capturedPiece = board.removePiece(enPassantVulnerable.getChessPosition().toPosition());
        }

        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
            piecesOnTheBoard.remove(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position origin, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        board.placePiece(p, origin);
        p.decreaseMoveCount();

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // Special Move: Castling
        // King Side Castling
        if (p instanceof King && target.getColumn() == origin.getColumn() + 2) {
            Position rookSource = new Position(origin.getRow(), origin.getColumn() + 3);
            Position rookTarget = new Position(target.getRow(), target.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }

        // Queen Side Castling
        if (p instanceof King && target.getColumn() == origin.getColumn() - 2) {
            Position rookSource = new Position(origin.getRow(), origin.getColumn() - 4);
            Position rookTarget = new Position(target.getRow(), target.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }

        // Special Move: En Passant
        if (p instanceof Pawn && capturedPiece == enPassantVulnerable && origin.getColumn() != target.getColumn()) {
            ChessPiece pawn = (ChessPiece) board.removePiece(target);
            int upOrDown = pawn.getColor() == Color.WHITE ? -1 : +1;
            target.setColumn(target.getColumn()+upOrDown);
            board.placePiece(pawn,target);
        }
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }


}
