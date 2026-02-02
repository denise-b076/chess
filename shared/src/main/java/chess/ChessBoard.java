package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard other) {
        this.squares = other.squares;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i, j);
                ChessGame.TeamColor color = (i <= 2) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                if (i > 2 && i < 7) {
                    addPiece(currPos, null);
                }
                else if (i == 2 || i == 7) {
                    addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.PAWN));
                }
                else {
                    if (j == 1 || j == 8) {
                        addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.ROOK));
                    } else if (j == 2 || j == 7) {
                        addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
                    } else if (j == 3 || j == 6) {
                        addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.BISHOP));
                    } else if (j == 4) {
                        addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.QUEEN));
                    } else {
                        addPiece(currPos, new ChessPiece(color, ChessPiece.PieceType.KING));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s", Arrays.toString(squares));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }
        ChessBoard p = (ChessBoard) o;
        return Objects.deepEquals(this.squares, p.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
