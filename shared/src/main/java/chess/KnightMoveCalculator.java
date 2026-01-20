package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.calculateMove;

public class KnightMoveCalculator {

    public KnightMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        KnightMoveCalculator calculator = new KnightMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        calculator.leftAndDown(possibleMoves, board, position);
        calculator.leftAndUp(possibleMoves, board, position);
        calculator.rightAndDown(possibleMoves, board, position);
        calculator.rightAndUp(possibleMoves, board, position);
        calculator.upAndLeft(possibleMoves, board, position);
        calculator.upAndRight(possibleMoves, board, position);
        calculator.downAndLeft(possibleMoves, board, position);
        calculator.downAndRight(possibleMoves, board, position);
        return possibleMoves;
    }

    private void leftAndUp(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 2);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void leftAndDown(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 2);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void rightAndUp(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 2);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void rightAndDown(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 2);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 2, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 2, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 2, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 2, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }
}
