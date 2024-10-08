package chess;
import java.util.Arrays;
import java.util.Objects;

/** A chessboard that can hold and rearrange chess pieces. */
public class ChessBoard {
    private final ChessPiece[][] Board;
    public ChessBoard() { this.Board = new ChessPiece[8][8]; }

    /** Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) { if (position.isValid()) { this.Board[position.getRow()-1][position.getColumn()-1] = piece; } }

    /** Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    public ChessPiece getPiece(ChessPosition position) { if (position.isValid()) { return this.Board[position.getRow()-1][position.getColumn()-1]; } return null; }

    /** Removes a chess piece from the chessboard
     * @param position The position to remove the piece from
     */
    public void removePiece(ChessPosition position) { this.Board[position.getRow()-1][position.getColumn()-1] = null; }

    /** Sets the board to the default starting board (How the game of chess normally starts) */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) { for (int j = 0; j < 8; j++) { this.Board[i][j] = null; } }
        this.Board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.Board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.Board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.Board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        this.Board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        this.Board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.Board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.Board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) { this.Board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN); }
        this.Board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.Board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.Board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.Board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.Board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        this.Board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.Board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.Board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) { this.Board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); }
    }

    @Override
    public String toString() {
        String representation = "[";
        for (int i = 0; i < 8; i++) {
            representation += Arrays.toString(this.Board[i]);
            if (i != 7) representation += ", ";
        }
        return representation + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return that.toString().equals(this.toString());
    }

    @Override
    public int hashCode() { return Arrays.deepHashCode(Board); }
}
