package chess;

import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves() {
        return List.of();
    }
}
