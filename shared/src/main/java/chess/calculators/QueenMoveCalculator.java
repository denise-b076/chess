package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator extends PieceMovesCalculator {

    public QueenMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        QueenMoveCalculator.upContinuous(possibleMoves, board, position);
        QueenMoveCalculator.downContinuous(possibleMoves, board, position);
        QueenMoveCalculator.leftContinuous(possibleMoves, board, position);
        QueenMoveCalculator.rightContinuous(possibleMoves, board, position);
        QueenMoveCalculator.upAndLeftContinuous(possibleMoves, board, position);
        QueenMoveCalculator.upAndRightContinuous(possibleMoves, board, position);
        QueenMoveCalculator.downAndLeftContinuous(possibleMoves, board, position);
        QueenMoveCalculator.downAndRightContinuous(possibleMoves, board, position);
        return possibleMoves;
    }
}
