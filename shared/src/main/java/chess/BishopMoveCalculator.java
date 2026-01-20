package chess;

import java.util.ArrayList;
import java.util.Collection;

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

    private boolean[] calculateMove(ChessPosition endPosition, ChessBoard board, ChessPosition startPosition) {
        boolean[] answers = new boolean[2];
        for (int i = 0; i < 2; i++) {
            answers[i] = true;
        }
        if (endPosition.getRow() > 8 || endPosition.getRow() < 1 || endPosition.getColumn() > 8 || endPosition.getColumn() < 1) {
            answers[0] = false;
            answers[1] = false;
            return answers;
        }
        ChessPiece startPiece = board.squares[startPosition.getRow() - 1][startPosition.getColumn() - 1];
        ChessPiece endPiece = board.squares[endPosition.getRow() - 1][endPosition.getColumn() - 1];
        if (endPiece != null) {
            answers[1] = false;
            answers[0] = endPiece.getTeamColor() != startPiece.getTeamColor();
            return answers;
        }
        return answers;
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        boolean edgeNotHit = true;
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        while (edgeNotHit) {
            ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
            boolean[] result = calculateMove(endPosition, board, startPosition);
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
            boolean[] result = calculateMove(endPosition, board, startPosition);
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
            boolean[] result = calculateMove(endPosition, board, startPosition);
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
            boolean[] result = calculateMove(endPosition, board, startPosition);
            if (result[0]) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            edgeNotHit = result[1];
            currRow -= 1;
            currCol += 1;
        }
    }
}
