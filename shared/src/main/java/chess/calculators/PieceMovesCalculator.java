package chess.calculators;

import chess.*;

import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator() {

    }

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return BishopMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return RookMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return QueenMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return KingMoveCalculator.calculateMoves(board, position);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return KnightMoveCalculator.calculateMoves(board, position);
        }
        else {
            return PawnMoveCalculator.calculateMoves(board, position);
        }
    }

    //the return booleans for answers are isGenerallyPossible, edgeOrPieceNotHit, and endPosHasNoPiece
    public static boolean[] calculateMove(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board) {
        boolean[] answers = new boolean[3];
        for (int i = 0; i < 3; i++) {
            answers[i] = true;
        }
        if (endPosition.getRow() > 8 || endPosition.getRow() < 1 || endPosition.getColumn() > 8 || endPosition.getColumn() < 1) {
            answers[0] = false;
            answers[1] = false;
            return answers;
        }
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);
        if (endPiece != null) {
            answers[0] = endPiece.getTeamColor() != startPiece.getTeamColor();
            answers[1] = false;
            answers[2] = false;
            return answers;
        }
        return answers;
    }

    public static void upContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void downContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void leftContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void rightContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void upAndLeftContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void upAndRightContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void downAndLeftContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

    public static void downAndRightContinuous(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition startPosition) {
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

