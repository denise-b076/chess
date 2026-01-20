package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator {

    public RookMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        RookMoveCalculator calculator = new RookMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        calculator.up(possibleMoves, board, position);
        calculator.down(possibleMoves, board, position);
        calculator.left(possibleMoves, board, position);
        calculator.right(possibleMoves, board, position);
        return possibleMoves;
    }

    private void up(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.upContinuous(possibleMoves, board, startPosition);
    }

    private void down(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.downContinuous(possibleMoves, board, startPosition);
    }

    private void left (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.leftContinuous(possibleMoves, board, startPosition);
    }

    private void right (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.rightContinuous(possibleMoves, board, startPosition);
    }
}
