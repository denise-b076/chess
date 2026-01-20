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
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return RookMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return QueenMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return KingMoveCalculator.calculateMoves(board, position);
        }
        return null;
    }

    public static boolean[] calculateMove(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board) {
        boolean[] answers = new boolean[2];
        for (int i = 0; i < 2; i++) {
            answers[i] = true;
        }
        if (endPosition.getRow() > 8 || endPosition.getRow() < 1 || endPosition.getColumn() > 8 || endPosition.getColumn() < 1) {
            answers[0] = false;
            answers[1] = false;
            return answers;
        }
        ChessPiece startPiece = board.squares[startPosition.getRow() - 1][startPosition.getColumn() - 1];
        ChessPiece endPiece = board.squares[endPosition.getRow() - 1][endPosition.getColumn() - 1];
        if (endPiece != null) {
            answers[1] = false;
            answers[0] = endPiece.getTeamColor() != startPiece.getTeamColor();
            return answers;
        }
        return answers;
    }
}
