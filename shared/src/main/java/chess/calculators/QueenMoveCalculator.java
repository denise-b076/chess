package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator {

    public QueenMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        QueenMoveCalculator calculator = new QueenMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        calculator.up(possibleMoves, board, position);
        calculator.down(possibleMoves, board, position);
        calculator.left(possibleMoves, board, position);
        calculator.right(possibleMoves, board, position);
        calculator.upAndLeft(possibleMoves, board, position);
        calculator.upAndRight(possibleMoves, board, position);
        calculator.downAndLeft(possibleMoves, board, position);
        calculator.downAndRight(possibleMoves, board, position);
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
