package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.calculators.PieceMovesCalculator.calculateMove;

public class RookMoveCalculator {

    public RookMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        RookMoveCalculator calculator = new RookMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        calculator.up(possibleMoves, board, position);
        calculator.down(possibleMoves, board, position);
        calculator.left(possibleMoves, board, position);
        calculator.right(possibleMoves, board, position);
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
}
