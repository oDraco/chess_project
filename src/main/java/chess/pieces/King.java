package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private final ChessMatch match;

    public King(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
    }

    private boolean canMove(Position position) {
        return getBoard().getPiece(position) == null || isThereOpponentPiece(position);
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().getPiece(position);
        return (p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0);
    }

    @Override
    public boolean[][] getPossibleMoves() {
        boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // 3 Positions above
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        for (int i = 0; i < 3; i++) {
            if (i > 0) p.setColumn(p.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        // 3 Position below
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        for (int i = 0; i < 3; i++) {
            if (i > 0) p.setColumn(p.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        // Left & Right
        p.setValues(position.getRow(), position.getColumn() - 1);
        for (int i = 0; i < 3; i += 2) {
            p.setColumn(p.getColumn() + i);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        // Special Move: Castling

        if (getMoveCount() == 0 && !match.getCheck()) {
            // king side castling
            p.setValues(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(p)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (getBoard().getPiece(p1) == null && getBoard().getPiece(p2) == null) {
                    moves[p2.getRow()][p2.getColumn()] = true;
                }
            }

            // queen side castling
            p.setValues(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(p)) {
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if (getBoard().getPiece(p1) == null && getBoard().getPiece(p2) == null && getBoard().getPiece(p3) == null) {
                    moves[p2.getRow()][p2.getColumn()] = true;
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "K";
    }


}
