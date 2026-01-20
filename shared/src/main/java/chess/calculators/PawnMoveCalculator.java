package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.calculators.PieceMovesCalculator.calculateMove;

public class PawnMoveCalculator {

    public PawnMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        PawnMoveCalculator calculator = new PawnMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(position);
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            calculator.up(possibleMoves, board, position, pawn);
            calculator.doubleUp(possibleMoves, board, position);
            calculator.upAndLeft(possibleMoves, board, position, pawn);
            calculator.upAndRight(possibleMoves, board, position, pawn);
        }
        else {
            calculator.down(possibleMoves, board, position, pawn);
            calculator.doubleDown(possibleMoves, board, position);
            calculator.downAndLeft(possibleMoves, board, position, pawn);
            calculator.downAndRight(possibleMoves, board, position, pawn);
        }
        return possibleMoves;
    }

    private void promotionLogic(Collection<ChessMove> possibleMoves, ChessPosition startPosition, ChessPosition endPosition, ChessPiece pawn) {
        int rowChecker = (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) ? 8 : 1;
        if (endPosition.getRow() == rowChecker) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        }
        else {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void up(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2]) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }

    private void doubleUp(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 2, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2] && startPosition.getRow() == 2 && board.getPiece(new ChessPosition(currRow + 1, currCol)) == null) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }

    private void down(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2]) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }

    private void doubleDown(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 2, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2] && startPosition.getRow() == 7 && board.getPiece(new ChessPosition(currRow - 1, currCol)) == null) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition, ChessPiece pawn) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            promotionLogic(possibleMoves, startPosition, endPosition, pawn);
        }
    }
}
