// Filename: Queen.java
//
// Contains the class Queen that represents a queen chesspiece
//
// Santrupti Nerli, Jan 2017

class Queen extends ChessPiece {

    // Default constructor sets row and col to infeasible (negative) values
    public Queen()
    {
        super();
    }

    // Constructor creates Queen with col c and row r
    public Queen(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
    // it basically captures the direction and location of the moves for queen
    // given the start and end locations square by squares that is recorded in validMoves (excluding start anf end positions)
    public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
      int length = 0;
      int addRow = 0;
      int addCol = 0;
      int[] validMoves;

      // this one is for the invalid move as well
      if ((startRow != destRow && startCol != destCol) && (Math.abs(startRow - destRow) != Math.abs(startCol - destCol))) {
        if(ignore == true) {
          return new int[0];
        }
        return null;
      }

      if(destRow == startRow) {
        length = Math.abs(destCol - startCol)-1;
        // direction down or up
        if(destCol < startCol) {
          addCol = -1;
        }
        else {
          addCol = 1;
        }
      }
      else if(destCol == startCol) {
        length = Math.abs(destRow - startRow)-1;
        // direction left or right
        if(destRow < startRow) {
          addRow = -1;
        }
        else {
          addRow = 1;
        }
      }
      else if(destRow < startRow && destCol < startCol) {
        length = Math.abs(destCol - startCol)-1;
         // direction left and down
         addRow = -1;
         addCol = -1;
      }
      else if(destRow < startRow && destCol > startCol) {
        length = Math.abs(destCol - startCol)-1;
        // direction right and down
        addRow = -1;
        addCol = 1;
      }
      else if(destRow > startRow && destCol < startCol) {
        length = Math.abs(destCol - startCol)-1;
        // direction left and down
        addRow = 1;
        addCol = -1;
      }
      else if(destRow > startRow && destCol > startCol) {
        length = Math.abs(destCol - startCol)-1;
        // direction left and up
        addRow = 1;
        addCol = 1;
      }

      // perform some more sanity check for invalid moves
      if(length < 0) {
        if(ignore == true) {
          return new int[0];
        }
        return null;
      }
      validMoves = new int[length*2];
      if(length == 0) {
        return validMoves;
      }

      // then actually populate the move steps
      validMoves[0] = startCol + addCol;
      validMoves[1] = startRow + addRow;
      for(int i = 2; i < length*2; i += 2) {
        validMoves[i] = validMoves[i-2] + addCol;
        validMoves[i+1] = validMoves[i-1] + addRow;
      }
        
      return validMoves;
    }

    // Boolean function that determines if self (which is a queen) is attacking another chesspiece, given as argument
    // Input: integer row and col
    // Output: True if self is attacking another chesspiece at (row, col), false otherwise
    public boolean isAttacking(ChessPiece piece)
    {
        if ((this.getRow() == piece.getRow() || this.getCol() == piece.getCol()) && (this.getColor() != piece.getColor())) // if self has same row or column as chesspiece, self is attacking
            return true;
        else if ((Math.abs(this.getRow() - piece.getRow()) == Math.abs(this.getCol() - piece.getCol())) && (this.getColor() != piece.getColor())) // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
            return true;
        else
            return false; // self is not attacking chesspiece
    }
}

// End
