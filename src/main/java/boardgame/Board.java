package boardgame;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1) {
            throw new BoardException("Invalid row/column data. There must be at least 1 row and 1 column.");
        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece getPiece(int row, int column) {
        if(!positionExists(row,column)) {
            throw new BoardException("Position not on the board.");
        }
        return pieces[row][column];
    }

    public Piece getPiece(Position position) {
        return getPiece(position.getRow(),position.getColumn());
    }

    public void placePiece(Piece piece, Position position) {
        if(thereIsAPiece(position)) {
            throw new BoardException("There's already a piece on position: " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece removePiece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Position not on the board.");
        }
        Piece piece = getPiece(position);
        if(piece == null) {
            return null;
        }
        piece.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return piece;
    }

    private boolean positionExists(int row, int column) {
        return (row >= 0 && column >= 0 && row < rows && column < columns);
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(),position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Position not on the board.");
        }
        return getPiece(position) != null;
    }
}
