// Filename: Bishop.java
//
// Contains the class Bishop that represents a bishop chesspiece
//
// Santrupti Nerli, Jan 2017

class Bishop extends ChessPiece {

  // Default constructor sets loc to infeasible (negative) values
  public Bishop()
  {
    super();
  }

  // Constructor creates Bishop at location (col, row) and color
  public Bishop(int row, int col, boolean color)
  {
    super(row, col, color);
  }

  // method to check if the start and end locations are correct
  // for example, we cannot make the bishop move along the same column
  // in such cases, this method would invalidate it by sending -1 for length of valid moves
  public int ifValidReturnLength(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
    int length = Math.abs(startRow - destRow)-1;
    if(length < 0 || (Math.abs(startRow - destRow) != Math.abs(startCol - destCol))) {
      if(ignore == true) {
        return 0;
      }
      return -1;
    }
    return length;
  }

  // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
  // it basically captures the direction and location of the moves for bishop
  // given the start and end locations square by squares that is recorded in validMoves (excluding start anf end positions)
  public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
    int length = ifValidReturnLength(startRow, startCol, destRow, destCol, ignore);
    if(length == -1) {
      return null;
    }
    int[] validMoves = new int[length*2];
    if(length == 0) {
      return validMoves;
    }

    int addRow = 0;
    int addCol = 0;
    if(destRow < startRow) {
      // direction left and down
      if(destCol < startCol) {
        addRow = -1;
        addCol = -1;
      }
      else {
        // direction left and up
        addRow = -1;
        addCol = 1;
      }
    }
    else {
      if(destCol < startCol) {
        // direction right and down
        addRow = 1;
        addCol = -1;
      }
      else {
        // direction right and up
        addRow = 1;
        addCol = 1;
      }
    }

    // record all the moves in between start and end destinations
    validMoves[0] = startCol + addCol;
    validMoves[1] = startRow + addRow;
    for(int i = 2; i < length*2; i += 2) {
      validMoves[i] = validMoves[i-2] + addCol;
      validMoves[i+1] = validMoves[i-1] + addRow;
    }

    return validMoves;
  }

  // Boolean function that determines if self (which is a bishop) is attacking another chesspiece at row and col, given as argument
  // Input: chess piece that is being checked for attack
  // Output: True if self is attacking the chesspiece at position row and col, false otherwise
  public boolean isAttacking(ChessPiece piece)
  {
      if ((Math.abs(this.getRow() - piece.getRow()) == Math.abs(this.getCol() - piece.getCol())) && (this.getColor() != piece.getColor())) // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
          return true;
      else
          return false; // self is not attacking chesspiece at position l
  }
}

// End
