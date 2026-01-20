package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator {

    public BishopMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        BishopMoveCalculator calculator = new BishopMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        calculator.upAndLeft(possibleMoves, board, position);
        calculator.upAndRight(possibleMoves, board, position);
        calculator.downAndLeft(possibleMoves, board, position);
        calculator.downAndRight(possibleMoves, board, position);
        return possibleMoves;
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.upAndLeftContinuous(possibleMoves, board, startPosition);
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.upAndRightContinuous(possibleMoves, board, startPosition);
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.downAndLeftContinuous(possibleMoves, board, startPosition);
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        PieceMovesCalculator.downAndRightContinuous(possibleMoves, board, startPosition);
    }
}
