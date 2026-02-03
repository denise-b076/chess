package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard currBoard;
    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        this.currBoard = new ChessBoard();
        currBoard.resetBoard();
    }

    public ChessGame(ChessGame other) {
        setTeamTurn(other.teamTurn);
        setBoard(other.currBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = currBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> basicMoves = piece.pieceMoves(currBoard, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();
        for(ChessMove move : basicMoves) {
            ChessGame testingGame = new ChessGame(this);
            testingGame.currBoard.makeMove(move);
            if (!testingGame.isInCheck(color)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece piece = currBoard.getPiece(startPosition);
        Collection<ChessMove> legalMoves = validMoves(startPosition);
        if (legalMoves == null || !legalMoves.contains(move) || piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("illegal move attempted");
        }
        else {
            ChessPosition endPosition = move.getEndPosition();
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
                    piece = new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.QUEEN);
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
                    piece = new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.BISHOP);
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
                    piece = new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.ROOK);
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) {
                    piece = new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.KNIGHT);
                }
            }
            currBoard.addPiece(endPosition, piece);
            currBoard.removePiece(startPosition);
            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor attackingTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition defendingKingPosition = kingLocator(teamColor);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = currBoard.getPiece(currPosition);
                if (currPiece != null && currPiece.getTeamColor() == attackingTeam) {
                    Collection<ChessMove> possibleMoves = currPiece.pieceMoves(currBoard, currPosition);
                    for (ChessMove move : possibleMoves) {
                        if (move.getEndPosition().equals(defendingKingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition kingLocator(ChessGame.TeamColor color) {
        ChessPosition possKingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                possKingPosition = new ChessPosition(i, j);
                ChessPiece currPiece = currBoard.getPiece(possKingPosition);
                if (currPiece != null && (currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == color)) {
                    return possKingPosition;
                }
            }
        }
        return possKingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currBoard = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currBoard;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", currBoard=" + currBoard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return getTeamTurn() == chessGame.getTeamTurn() && Objects.equals(currBoard, chessGame.currBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamTurn(), currBoard);
    }
}
