package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        if (row >=0 && row <= 7) { this.row = row; }
        else { this.row = -1; }
        if (col >=0 && col <= 7) { this.col = col; }
        else { this.col = -1; }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() { return this.row; }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() { return this.col; }

    /**
     * @return true if position is valid, false otherwise
     */
    public boolean isValid() { return (this.row >= 0 && this.col >= 0); }

    @Override
    public String toString() { return "ChessPosition{" + "row=" + row + ", col=" + col + '}'; }
}
