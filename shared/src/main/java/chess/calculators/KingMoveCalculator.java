package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.calculators.PieceMovesCalculator.calculateMove;

public class KingMoveCalculator {

    public KingMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        KingMoveCalculator calculator = new KingMoveCalculator();
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
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void down(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void left (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void right (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }
}
