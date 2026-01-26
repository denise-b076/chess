package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends PieceMovesCalculator{

    public BishopMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        BishopMoveCalculator.upAndLeftContinuous(possibleMoves, board, position);
        BishopMoveCalculator.upAndRightContinuous(possibleMoves, board, position);
        BishopMoveCalculator.downAndLeftContinuous(possibleMoves, board, position);
        BishopMoveCalculator.downAndRightContinuous(possibleMoves, board, position);
        return possibleMoves;
    }
}
