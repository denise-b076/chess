package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    private String parsePosition() {
        String colRepresentation = "";
        switch(col) {
            case 1 -> colRepresentation = "a";
            case 2 -> colRepresentation = "b";
            case 3 -> colRepresentation = "c";
            case 4 -> colRepresentation = "d";
            case 5 -> colRepresentation = "e";
            case 6 -> colRepresentation = "f";
            case 7 -> colRepresentation = "g";
            case 8 -> colRepresentation = "h";
        }
        String rowRepresentation = Integer.toString(row);
        return colRepresentation + rowRepresentation;
    }

    @Override
    public String toString() {
//        return String.format("[%d,%d]", row, col);
        return parsePosition();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }

        ChessPosition p = (ChessPosition) o;
        return Objects.equals(getRow(),p.getRow()) && Objects.equals(getColumn(),p.getColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
