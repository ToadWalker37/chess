package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type;
    private ChessGame.TeamColor color;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPosition[] validMoves = new ChessPosition[64];
        switch (this.type) {
            case KING:
                int totalValidMoves = 0;
                ChessPosition[] possibleMoves = new ChessPosition[8];
                possibleMoves[0] = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                possibleMoves[1] = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                possibleMoves[2] = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                possibleMoves[3] = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
                possibleMoves[4] = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
                possibleMoves[5] = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                possibleMoves[6] = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                possibleMoves[7] = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                for (ChessPosition possibleMove : possibleMoves) {
                    if (possibleMove.isValid() && board.getPiece(possibleMove) == null) {
                        validMoves[totalValidMoves] = possibleMove;
                        totalValidMoves++;
                    }
                }
                break;
            case QUEEN:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }
        return validMoves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" + "type=" + type + ", color=" + color + '}';
    }
}
