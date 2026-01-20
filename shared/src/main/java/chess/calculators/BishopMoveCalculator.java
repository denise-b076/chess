package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.calculators.PieceMovesCalculator.calculateMove;

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
