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
        calculator.upOne(possibleMoves, board, position);
        calculator.downOne(possibleMoves, board, position);
        calculator.leftOne(possibleMoves, board, position);
        calculator.rightOne(possibleMoves, board, position);
        calculator.upAndLeftOne(possibleMoves, board, position);
        calculator.upAndRightOne(possibleMoves, board, position);
        calculator.downAndLeftOne(possibleMoves, board, position);
        calculator.downAndRightOne(possibleMoves, board, position);
        return possibleMoves;
    }

    private void upOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void leftOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void rightOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndLeftOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndRightOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndLeftOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndRightOne(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0]) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }
}
