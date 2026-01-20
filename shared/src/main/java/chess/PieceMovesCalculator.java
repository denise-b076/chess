package chess;

import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
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
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return KnightMoveCalculator.calculateMoves(board, position);
        }
        else {
            return PawnMoveCalculator.calculateMoves(board, position);
        }
    }

    //the return booleans for answers are isGenerallyPossible, edgeOrPieceNotHit, and endPosHasNoPiece
    public static boolean[] calculateMove(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board) {
        boolean[] answers = new boolean[3];
        for (int i = 0; i < 3; i++) {
            answers[i] = true;
        }
        if (endPosition.getRow() > 8 || endPosition.getRow() < 1 || endPosition.getColumn() > 8 || endPosition.getColumn() < 1) {
            answers[0] = false;
            answers[1] = false;
            return answers;
        }
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);
        if (endPiece != null) {
            answers[0] = endPiece.getTeamColor() != startPiece.getTeamColor();
            answers[1] = false;
            answers[2] = false;
            return answers;
        }
        return answers;
    }
}

