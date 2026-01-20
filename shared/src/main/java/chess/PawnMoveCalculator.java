package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.calculateMove;

public class PawnMoveCalculator {

    public PawnMoveCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        PawnMoveCalculator calculator = new PawnMoveCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(position);
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            calculator.up(possibleMoves, board, position);
            calculator.doubleUp(possibleMoves, board, position);
            calculator.upAndLeft(possibleMoves, board, position);
            calculator.upAndRight(possibleMoves, board, position);
        }
        else {
            calculator.down(possibleMoves, board, position);
            calculator.doubleDown(possibleMoves, board, position);
            calculator.downAndLeft(possibleMoves, board, position);
            calculator.downAndRight(possibleMoves, board, position);
        }
        return possibleMoves;
    }

    private void up(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2]) {
            if (endPosition.getRow() == 8) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void doubleUp(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 2, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2] && startPosition.getRow() == 2 && board.squares[currRow][currCol - 1] == null) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void upAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            if (endPosition.getRow() == 8) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void upAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow + 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            if (endPosition.getRow() == 8) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void down(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2]) {
            if (endPosition.getRow() == 1) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void doubleDown(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 2, currCol);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && result[2] && startPosition.getRow() == 7 && board.squares[currRow - 2][currCol - 1] == null) {
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    private void downAndLeft(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol - 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            if (endPosition.getRow() == 1) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void downAndRight(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(currRow - 1, currCol + 1);
        boolean[] result = calculateMove(startPosition, endPosition, board);
        if (result[0] && !(result[2])) {
            if (endPosition.getRow() == 1) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }
}
