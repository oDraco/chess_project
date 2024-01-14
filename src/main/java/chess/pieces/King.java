package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    private boolean canMove(Position position) {
        return getBoard().getPiece(position) == null || isThereOpponentPiece(position);
    }

    @Override
    public boolean[][] getPossibleMoves() {
        boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // 3 Positions above
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        for (int i = 0; i < 3; i++) {
            if(i>0) p.setColumn(p.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        // 3 Position below
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        for (int i = 0; i < 3; i++) {
            if(i>0) p.setColumn(p.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        // Left & Right
        p.setValues(position.getRow(), position.getColumn() - 1);
        for(int i=0; i<3; i+=2) {
            p.setColumn(p.getColumn()+i);
            if (getBoard().positionExists(p) && canMove(p)) {
                moves[p.getRow()][p.getColumn()] = true;
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "K";
    }


}
