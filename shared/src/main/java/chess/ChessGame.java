package chess;
import java.util.ArrayList;
import java.util.Collection;

/** For a class that can manage a chess game, making moves on a board */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /** @return Which team's turn it is */
    public TeamColor getTeamTurn() { return this.teamTurn; }

    /** Sets which team's turn it is
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { this.teamTurn = team; }

    /** Enum identifying the 2 possible teams in a chess game */
    public enum TeamColor { WHITE, BLACK }

    /** Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece thisPiece = this.board.getPiece(startPosition);
        if (thisPiece == null) { return null; }
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> possibleMoves = thisPiece.pieceMoves(this.board, startPosition);
        for (ChessMove possibleMove : possibleMoves) {
            ChessPiece targetedPiece = this.board.getPiece(possibleMove.getEndPosition());
            this.board.addPiece(possibleMove.getEndPosition(), thisPiece);
            this.board.removePiece(possibleMove.getStartPosition());
            if (!isInCheck(thisPiece.getTeamColor()) && !isInStalemate(thisPiece.getTeamColor())) { validMoves.add(possibleMove); }
            this.board.addPiece(possibleMove.getStartPosition(), thisPiece);
            this.board.addPiece(possibleMove.getEndPosition(), targetedPiece);
        }
        return validMoves;
    }

    /** Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (
                this.board.getPiece(move.getStartPosition()) != null &&
                this.teamTurn == this.board.getPiece(move.getStartPosition()).getTeamColor() &&
                this.validMoves(move.getStartPosition()) != null &&
                this.validMoves(move.getStartPosition()).contains(move)
        ) {
            if (move.getPromotionPiece() == null) { this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition())); }
            else { this.board.addPiece(move.getEndPosition(), new ChessPiece(this.board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece())); }
            this.board.removePiece(move.getStartPosition());
            if (this.teamTurn == TeamColor.WHITE) { setTeamTurn(TeamColor.BLACK); }
            else { setTeamTurn(TeamColor.WHITE); }
        }
        else { throw new InvalidMoveException(); }
    }

    /** Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                Collection<ChessMove> possibleMoves;
                if (this.board.getPiece(new ChessPosition(row, col)) != null) {
                    possibleMoves = this.board.getPiece(new ChessPosition(row, col)).pieceMoves(this.board, new ChessPosition(row, col));
                } else { possibleMoves = new ArrayList<>(); }
                Collection<ChessMove> possibleOpposingTeamMoves = new ArrayList<>();
                for (ChessMove move : possibleMoves) {
                    if (board.getPiece(move.getStartPosition()) != null && board.getPiece(move.getStartPosition()).getTeamColor() != teamColor) { possibleOpposingTeamMoves.add(move); }
                }
                for (ChessMove move : possibleOpposingTeamMoves) {
                    ChessPiece homeTeamPiece = board.getPiece(move.getEndPosition());
                    if (homeTeamPiece != null && homeTeamPiece.getPieceType() == ChessPiece.PieceType.KING) { return true; }
                }
            }
        }
        return false;
    }

    /** Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate/stalemate
     * @param mate whether the team is going to have no future next turn
     * @return True if the specified team will have no possible moves, false otherwise
     */
    private boolean isFutureViable(TeamColor teamColor, boolean mate) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                Collection<ChessMove> possibleMoves;
                if (this.board.getPiece(new ChessPosition(row, col)) != null) {
                    possibleMoves = this.board.getPiece(new ChessPosition(row, col)).pieceMoves(this.board, new ChessPosition(row, col));
                } else { possibleMoves = new ArrayList<>(); }
                Collection<ChessMove> possibleHomeTeamMoves = new ArrayList<>();
                for (ChessMove move : possibleMoves) {
                    if (board.getPiece(move.getStartPosition()) != null && board.getPiece(move.getStartPosition()).getTeamColor() == teamColor) { possibleHomeTeamMoves.add(move); }
                }
                for (ChessMove move : possibleHomeTeamMoves) {
                    ChessPiece thisPiece = board.getPiece(move.getStartPosition());
                    ChessPiece targetedPiece = board.getPiece(move.getEndPosition());
                    this.board.addPiece(move.getEndPosition(), thisPiece);
                    this.board.removePiece(move.getStartPosition());
                    if (!isInCheck(thisPiece.getTeamColor())) { mate = false; }
                    this.board.addPiece(move.getStartPosition(), thisPiece);
                    this.board.addPiece(move.getEndPosition(), targetedPiece);
                }
            }
        }
        return mate;
    }

    /** Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = true;
        return this.isInCheck(teamColor) && isFutureViable(teamColor, checkmate);
    }

    /** Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = true;
        return !this.isInCheck(teamColor) && isFutureViable(teamColor, stalemate);
    }

    /** Sets this game's chessboard with a given board
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board; }

    /** Gets the current chessboard
     * @return the chessboard
     */
    public ChessBoard getBoard() { return this.board; }
}
