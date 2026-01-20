package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.calculateMove;

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
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow + 1, currCol);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow += 1;
        }
    }

    private void down(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow - 1, currCol);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow -= 1;
        }
    }

    private void left (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow, currCol - 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currCol -= 1;
        }
    }

    private void right (Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow, currCol + 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currCol += 1;
        }
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow += 1;
            currCol -= 1;
        }
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow += 1;
            currCol += 1;
        }
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow -= 1;
            currCol -= 1;
        }
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 1);
            boolean[] result = calculateMove(startPosition, endPosition, board);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow -= 1;
            currCol += 1;
        }
    }

}
