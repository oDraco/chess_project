package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private final ChessMatch match;
    public Pawn(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
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

        // Special Move: En Passant
        upOrDown = position.getRow() == 3 ? -1 : +1;
        if(position.getRow() == 3 || position.getRow() == 4) {
            Position left = new Position(position.getRow(),position.getColumn()-1);
            Position right = new Position(position.getRow(),position.getColumn()+1);
            if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().getPiece(left) == match.getEnPassantVulnerable()) {
                moves[left.getRow()+upOrDown][left.getColumn()] = true;
            }
            if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().getPiece(right) == match.getEnPassantVulnerable()) {
                moves[right.getRow()+upOrDown][right.getColumn()] = true;
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "P";
    }
}
