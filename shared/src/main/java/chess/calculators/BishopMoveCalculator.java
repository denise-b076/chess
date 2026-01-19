package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;

public class BishopMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves() {
        return List.of();
    }
}
