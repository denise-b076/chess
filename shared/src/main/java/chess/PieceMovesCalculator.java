package chess;

import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.squares[position.getRow() - 1][position.getColumn() - 1];
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return BishopMoveCalculator.calculateMoves(board, position);
        }
        return null;
    }
}
