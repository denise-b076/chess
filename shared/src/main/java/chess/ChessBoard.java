package chess;

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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (i > 2 && i < 7) {
                    squares[i - 1][j - 1] = null;
                }
                else {
                    if (i == 2 || i == 7) {
                        squares[i - 1][j - 1] = new ChessPiece((i == 2) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                    }
                    else {
                        if (j == 1 || j == 8) {
                            squares[i - 1][j - 1] = new ChessPiece((i == 1) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                        } else if (j == 2 || j == 7) {
                            squares[i - 1][j - 1] = new ChessPiece((i == 1) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                        } else if (j == 3 || j == 6) {
                            squares[i - 1][j - 1] = new ChessPiece((i == 1) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                        } else if (j == 4) {
                            squares[i - 1][j - 1] = new ChessPiece((i == 1) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                        } else {
                            squares[i - 1][j - 1] = new ChessPiece((i == 1) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                        }
                    }
                }
            }
        }
    }
}
