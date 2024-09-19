package chess;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;

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
     * @return null if move is invalid, or the proper move given the rows and columns desired to move
     */
    public ChessMove formulateMove(ChessBoard board, ChessPosition position, int rowOffset, int colOffset) {
        ChessPosition newPosition = new ChessPosition(position.getRow()+rowOffset, position.getColumn()+colOffset);
        if (!newPosition.isValid()) { return null; } // if move takes you off the board
        if (abs(rowOffset) == abs(colOffset)) { // if move is diagonal and there's a blocking piece
            for (int i = 0; i < abs(rowOffset); i++) {
                if (board.getPiece(new ChessPosition(position.getRow()+i, position.getColumn()+i)) != null) { return null; }
            }
        }
        if (rowOffset == 0 || colOffset == 0) { // if move is straight and there's a blocking piece
            for (int i = 0; i < abs(rowOffset+colOffset); i++) {
                if (board.getPiece(new ChessPosition(position.getRow()+i, position.getColumn()+i)) != null) { return null; }
            }
        }
        if (this.type == PieceType.PAWN) { return new ChessMove(position, newPosition, PieceType.QUEEN); }
        return new ChessMove(position, newPosition, null);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        switch (this.type) {
            case KING:
                possibleMoves.add(formulateMove(board, myPosition, -1, -1));
                possibleMoves.add(formulateMove(board, myPosition, -1, 0));
                possibleMoves.add(formulateMove(board, myPosition, -1, 1));
                possibleMoves.add(formulateMove(board, myPosition, 0, -1));
                possibleMoves.add(formulateMove(board, myPosition, 0, 1));
                possibleMoves.add(formulateMove(board, myPosition, 1, -1));
                possibleMoves.add(formulateMove(board, myPosition, 1, 0));
                possibleMoves.add(formulateMove(board, myPosition, 1, 1));
                break;
            case QUEEN:
                for (int i = -7; i < 8; i++) { // Straight up and down
                    possibleMoves.add(formulateMove(board, myPosition, i, 0));
                }
                for (int i = -7; i < 8; i++) { // Straight left and right
                    possibleMoves.add(formulateMove(board, myPosition, 0, i));
                }
                for (int i = -7; i < 8; i++) { // Diagonal Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, i));
                }
                for (int i = -7; i < 8; i++) { // Diagonal opposite-Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, -i));
                }
                break;
            case BISHOP:
                for (int i = -7; i < 8; i++) { // Diagonal Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, i));
                }
                for (int i = -7; i < 8; i++) { // Diagonal opposite-Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, -i));
                }
                break;
            case KNIGHT:
                possibleMoves.add(formulateMove(board, myPosition, 1, -2));
                possibleMoves.add(formulateMove(board, myPosition, 1, 2));
                possibleMoves.add(formulateMove(board, myPosition, -1, -2));
                possibleMoves.add(formulateMove(board, myPosition, -1, 2));
                possibleMoves.add(formulateMove(board, myPosition, 2, -1));
                possibleMoves.add(formulateMove(board, myPosition, 2, 1));
                possibleMoves.add(formulateMove(board, myPosition, -2, -1));
                possibleMoves.add(formulateMove(board, myPosition, -2, 1));
                break;
            case ROOK:
                for (int i = -7; i < 8; i++) { // Straight up and down
                    possibleMoves.add(formulateMove(board, myPosition, i, 0));
                }
                for (int i = -7; i < 8; i++) { // Straight left and right
                    possibleMoves.add(formulateMove(board, myPosition, 0, i));
                }
                break;
            case PAWN:
                break;
        }
        for (ChessMove possibleMove : possibleMoves) {
            if (possibleMove != null) { validMoves.add(possibleMove); }
        }
        return validMoves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" + "type=" + type + ", color=" + color + '}';
    }
}
