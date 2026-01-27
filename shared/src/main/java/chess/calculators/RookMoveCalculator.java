package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMovesCalculator {

    public RookMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        RookMoveCalculator.upContinuous(possibleMoves, board, position);
        RookMoveCalculator.downContinuous(possibleMoves, board, position);
        RookMoveCalculator.leftContinuous(possibleMoves, board, position);
        RookMoveCalculator.rightContinuous(possibleMoves, board, position);
        return possibleMoves;
    }

}
