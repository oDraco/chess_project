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
}
