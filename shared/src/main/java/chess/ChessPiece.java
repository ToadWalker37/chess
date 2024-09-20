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
    public ChessMove formulateMove(ChessBoard board, ChessPosition position, int rowOffset, int colOffset, PieceType promotionPiece) {
        ChessPosition newPosition = new ChessPosition(position.getRow()+rowOffset, position.getColumn()+colOffset);
        if (!newPosition.isValid() || position.equals(newPosition)) { return null; } // if move takes you off the board or doesn't take you anywhere
        if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == this.color) { return null; } // if you would be taking a piece of your own color
        if (this.type == PieceType.PAWN) {
            boolean promotion = false;
            if (abs(rowOffset) > 1 && position.getRow() != 7 && this.color == ChessGame.TeamColor.BLACK) { return null; } // disallow double move if black pawn has been moved
            if (abs(rowOffset) > 1 && position.getRow() != 2 && this.color == ChessGame.TeamColor.WHITE) { return null; } // disallow double move if white pawn has been moved
            for (int i = 1; i <= abs(rowOffset); i++) {
                if (this.color == ChessGame.TeamColor.BLACK) {
                    if (colOffset == 0 && board.getPiece(new ChessPosition(position.getRow() - i, position.getColumn())) != null) { // if blocking piece
                        return null;
                    }
                }
                if (this.color == ChessGame.TeamColor.WHITE) {
                    if (colOffset == 0 && board.getPiece(new ChessPosition(position.getRow() + i, position.getColumn())) != null) { // if blocking piece
                        return null;
                    }
                }
            }
            if (abs(colOffset) > 0 && this.color == ChessGame.TeamColor.BLACK) {
                ChessPiece pieceKittyCorner = board.getPiece(new ChessPosition(position.getRow()+rowOffset, position.getColumn()+colOffset));
                if (pieceKittyCorner == null || pieceKittyCorner.color == ChessGame.TeamColor.BLACK) { // if no white piece to capture
                    return null;
                }
            }
            if (abs(colOffset) > 0 && this.color == ChessGame.TeamColor.WHITE) {
                ChessPiece pieceKittyCorner = board.getPiece(new ChessPosition(position.getRow()+rowOffset, position.getColumn()+colOffset));
                if (pieceKittyCorner == null || pieceKittyCorner.color == ChessGame.TeamColor.WHITE) { // if no black piece to capture
                    return null;
                }
            }
            if ((this.color == ChessGame.TeamColor.BLACK && newPosition.getRow() == 1) || (this.color == ChessGame.TeamColor.WHITE && newPosition.getRow() == 8)) { // promotion
                promotion = true;
            }
            if (promotion) { return new ChessMove(position, newPosition, promotionPiece); }
            else { return new ChessMove(position, newPosition, null); }
        }
        else {
            if (abs(rowOffset) == abs(colOffset)) { // if move is diagonal
                if (rowOffset < 0) {
                    if (colOffset < 0) {
                        for (int i = 1; i < abs(rowOffset); i++) {
                            if (board.getPiece(new ChessPosition(position.getRow() - i, position.getColumn() - i)) != null) {
                                return null;
                            } // if there's a blocking piece
                        }
                    } else {
                        for (int i = 1; i < abs(rowOffset); i++) {
                            if (board.getPiece(new ChessPosition(position.getRow() - i, position.getColumn() + i)) != null) {
                                return null;
                            } // if there's a blocking piece
                        }
                    }
                } else {
                    if (colOffset < 0) {
                        for (int i = 1; i < abs(rowOffset); i++) {
                            if (board.getPiece(new ChessPosition(position.getRow() + i, position.getColumn() - i)) != null) {
                                return null;
                            } // if there's a blocking piece
                        }
                    } else {
                        for (int i = 1; i < abs(rowOffset); i++) {
                            if (board.getPiece(new ChessPosition(position.getRow() + i, position.getColumn() + i)) != null) {
                                return null;
                            } // if there's a blocking piece
                        }
                    }
                }
            }
            if (rowOffset == 0) {
                if (colOffset < 0) { // if the move is going left
                    for (int i = 0; i < abs(rowOffset + colOffset); i++) {
                        ChessPosition tempPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
                        if (!tempPosition.equals(position) && board.getPiece(tempPosition) != null) {
                            return null;
                        }
                    }
                } else { // if the move is going right
                    for (int i = 0; i < abs(rowOffset + colOffset); i++) {
                        ChessPosition tempPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
                        if (!tempPosition.equals(position) && board.getPiece(tempPosition) != null) {
                            return null;
                        }
                    }
                }
            }
            if (colOffset == 0) { // if move is straight and there's a blocking piece
                if (rowOffset < 0) { // if the move is going down
                    for (int i = 0; i < abs(rowOffset + colOffset); i++) {
                        ChessPosition tempPosition = new ChessPosition(position.getRow() - i, position.getColumn());
                        if (!tempPosition.equals(position) && board.getPiece(tempPosition) != null) {
                            return null;
                        }
                    }
                } else { // if the move is going up
                    for (int i = 0; i < abs(rowOffset + colOffset); i++) {
                        ChessPosition tempPosition = new ChessPosition(position.getRow() + i, position.getColumn());
                        if (!tempPosition.equals(position) && board.getPiece(tempPosition) != null) {
                            return null;
                        }
                    }
                }
            }
            return new ChessMove(position, newPosition, null);
        }
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
                possibleMoves.add(formulateMove(board, myPosition, -1, -1, null));
                possibleMoves.add(formulateMove(board, myPosition, -1, 0, null));
                possibleMoves.add(formulateMove(board, myPosition, -1, 1, null));
                possibleMoves.add(formulateMove(board, myPosition, 0, -1, null));
                possibleMoves.add(formulateMove(board, myPosition, 0, 1, null));
                possibleMoves.add(formulateMove(board, myPosition, 1, -1, null));
                possibleMoves.add(formulateMove(board, myPosition, 1, 0, null));
                possibleMoves.add(formulateMove(board, myPosition, 1, 1, null));
                break;
            case QUEEN:
                for (int i = -7; i < 8; i++) { // Straight up and down
                    possibleMoves.add(formulateMove(board, myPosition, i, 0, null));
                }
                for (int i = -7; i < 8; i++) { // Straight left and right
                    possibleMoves.add(formulateMove(board, myPosition, 0, i, null));
                }
                for (int i = -7; i < 8; i++) { // Diagonal Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, i, null));
                }
                for (int i = -7; i < 8; i++) { // Diagonal opposite-Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, -i, null));
                }
                break;
            case BISHOP:
                for (int i = -7; i < 8; i++) { // Diagonal Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, i, null));
                }
                for (int i = -7; i < 8; i++) { // Diagonal opposite-Volvo-logo-style
                    possibleMoves.add(formulateMove(board, myPosition, i, -i, null));
                }
                break;
            case KNIGHT:
                possibleMoves.add(formulateMove(board, myPosition, 1, -2, null));
                possibleMoves.add(formulateMove(board, myPosition, 1, 2, null));
                possibleMoves.add(formulateMove(board, myPosition, -1, -2, null));
                possibleMoves.add(formulateMove(board, myPosition, -1, 2, null));
                possibleMoves.add(formulateMove(board, myPosition, 2, -1, null));
                possibleMoves.add(formulateMove(board, myPosition, 2, 1, null));
                possibleMoves.add(formulateMove(board, myPosition, -2, -1, null));
                possibleMoves.add(formulateMove(board, myPosition, -2, 1, null));
                break;
            case ROOK:
                for (int i = -7; i < 8; i++) { // Straight up and down
                    possibleMoves.add(formulateMove(board, myPosition, i, 0, null));
                }
                for (int i = -7; i < 8; i++) { // Straight left and right
                    possibleMoves.add(formulateMove(board, myPosition, 0, i, null));
                }
                break;
            case PAWN:
                for (PieceType pieceType : PieceType.values()) {
                    if (pieceType != PieceType.KING && pieceType != PieceType.PAWN) {
                        if (this.color == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(formulateMove(board, myPosition, -1, 0, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, -2, 0, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, -1, -1, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, -1, 1, pieceType));
                        } else {
                            possibleMoves.add(formulateMove(board, myPosition, 1, 0, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, 2, 0, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, 1, -1, pieceType));
                            possibleMoves.add(formulateMove(board, myPosition, 1, 1, pieceType));
                        }
                    }
                }
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
