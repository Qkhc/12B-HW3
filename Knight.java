// Filename: Knight.java
//
// Contains the class Knight that represents a knight chesspiece
//
// Santrupti Nerli, Jan 2017

class Knight extends ChessPiece {

    private static final int movesRow[] = {-1, 1, -1, 1, -2, -2, 2, 2}; // possible attack row positions
    private static final int movesCol[] = {-2, -2, 2, 2, -1, 1, -1, 1}; // possible attack col positions

    // Default constructor sets location to infeasible (negative) values
    public Knight()
    {
      super();
    }

    // Constructor creates Knight with row, col and color
    public Knight(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // method that checks if the end position is a valid knight move or not
    public int ifValidReturnLength(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
      boolean isValid = false;

      for(int i = 0; i < movesRow.length; i++) {
        if(startRow + movesRow[i] == destRow && startCol + movesCol[i] == destCol) {
          isValid = true;
        }
      }

      if(!isValid) {
        if(ignore == true) {
          return 0;
        }
        return -1;
      }
      return 0;
    }

    // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
    // for knight it does not really matter, all we need to check are if the moves are valid
    // additional checks are performed by the ifValidReturnLength
    public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore){
      int[] validMoves = new int[0];
      int length = ifValidReturnLength(startRow, startCol, destRow, destCol, ignore);
      if(length == -1) {
        return null;
      }
      return validMoves;
    }

    // Boolean function that determines if self (which is a knight) is attacking another chesspiece, given as argument
    // Input: integer row, col
    // Output: True if self is attacking chesspiece at location (row, col), false otherwise
    public boolean isAttacking(ChessPiece piece)
    {
        int attackRow[] = {-1, 1, -1, 1, -2, -2, 2, 2}; // possible attack row positions
        int attackCol[] = {-2, -2, 2, 2, -1, 1, -1, 1}; // possible attack col positions

        for(int i = 0; i < 8; i++) {
          if((this.getRow() + attackRow[i] == piece.getRow() && this.getCol() + attackCol[i] == piece.getCol()) && (this.getColor() != piece.getColor())) {
            return true;
          }
        }
        return false;
    }
}

// End
