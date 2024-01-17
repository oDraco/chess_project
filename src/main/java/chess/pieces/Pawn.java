package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] getPossibleMoves() {
        boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        int upOrDown = getColor() == Color.WHITE ? -1 : +1;
        p.setValues(position.getRow() + upOrDown, position.getColumn());
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            moves[p.getRow()][p.getColumn()] = true;
            p.setValues(position.getRow() + 2 * upOrDown, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount() == 0) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }
        p.setValues(position.getRow() + upOrDown, position.getColumn() - 1);
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            moves[p.getRow()][p.getColumn()] = true;
        }
        p.setColumn(position.getColumn() + 1);
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            moves[p.getRow()][p.getColumn()] = true;
        }

        return moves;
    }

    @Override
    public String toString() {
        return "P";
    }
}
