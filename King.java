// Filename: King.java
//
// Contains the class King that represents a king chesspiece
//
// Santrupti Nerli, Jan 2016

class King extends ChessPiece {

    private final static int movesRow[] = {-1, -1, 0, 1, 0, 1, 1, -1}; // possible attack and move row positions
    private final static int movesCol[] = {0, -1, -1, -1, 1, 1, 0, 1}; // possible attack and move col positions

    // Default constructor sets row and col to infeasible (negative) values
    public King()
    {
        super();
    }

    // Constructor creates King with given row, col and color
    public King(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // method that checks if the end position lies in any of the 8 positions surrounding the start location
    // out of the board checks are performed by the caller
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
    // for king it does not really matter, all we need to check are if the moves are valid
    // additional checks are performed by the ifValidReturnLength
    public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore){
      int[] validMoves = new int[0];
      int length = ifValidReturnLength(startRow, startCol, destRow, destCol, ignore);
      if(length == -1) {
        return null;
      }
      return validMoves;
    }

    // Boolean function that determines if self (which is a king) is attacking another chesspiece, given as argument
    // Input: integer row, col
    // Output: True if self is attacking chesspiece at location (row, col), false otherwise
    public boolean isAttacking(ChessPiece piece)
    {

        for(int i = 0; i < 8; i++) {
          if((this.getRow() + movesRow[i] == piece.getRow() && this.getCol() + movesCol[i] == piece.getCol()) && (this.getColor() != piece.getColor())) {
            return true;
          }
        }
        return false;
    }
}

// End
