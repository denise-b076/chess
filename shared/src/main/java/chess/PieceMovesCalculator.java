package chess;

import chess.calculators.BishopMoveCalculator;

import java.util.Collection;

public class PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves() {
        ChessPiece piece = board.squares[position.getRow() - 1][position.getColumn() - 1];
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            BishopMoveCalculator bishopMoveCalculator = new BishopMoveCalculator(board, position);
            return bishopMoveCalculator.calculateMoves();
        }
        return null;
    }
}
