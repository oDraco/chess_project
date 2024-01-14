package boardgame;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null; // Not necessary (not initialized == null). The piece hasn't been put in board.
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] getPossibleMoves();

    public boolean getPossibleMove(Position position) {
        return getPossibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] moves = getPossibleMoves();
        for(int i=0; i<moves.length; i++) {
            for(int j=0; j<moves[i].length; j++) {
                if(moves[i][j]) return true;
            }
        }
        return false;
    }
}
