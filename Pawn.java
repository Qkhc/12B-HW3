// Filename: Pawn.java
//
// Contains the class Pawn that represents a pawn chesspiece
//
// Santrupti Nerli, Jan 2017

class Pawn extends ChessPiece {

  // Default constructor sets loc to infeasible (negative) values
  public Pawn()
  {
    super();
  }

  // Constructor creates Pawn with col, row and color
  public Pawn(int row, int col, boolean color)
  {
    super(row, col, color);
  }

  // method to return attack rows for pawns of different color
  public int[] getAttackRow() {
    int[] movesRow = {0, 0};
    if(this.getColor() == true) {
      // if it is a white pawn
      // then it advances from bottom to top (unidirectional) in a chessboard
      // attacks only one step diagonals
      movesRow[0] = 1;
      movesRow[1] = 1;
    }
    else if(this.getColor() == false) {
      // if it is a black pawn
      // then it advances from top to bottom (unidirectional) in a chessboard
      // attacks only one step diagonals
      movesRow[0] = -1;
      movesRow[1] = -1;
    }
    return movesRow;
  }

  // method to return attack cols for pawns of different color
  public int[] getAttackCol() {
    int[] movesCol = {0, 0};
    if(this.getColor() == true) {
      // if it is a white pawn
      // then it advances from bottom to top (unidirectional) in a chessboard
      // attacks only one step diagonals
      movesCol[0] = -1;
      movesCol[1] = 1;
    }
    else if(this.getColor() == false) {
      // if it is a black pawn
      // then it advances from top to bottom (unidirectional) in a chessboard
      // attacks only one step diagonals
      movesCol[0] = -1;
      movesCol[1] = 1;
    }
    return movesCol;
  }

  // Method to check is the straight move is just by one square or not
  // given the start and end locations
  public boolean checkStraightMove(int startRow, int startCol, int destRow, int destCol) {
    if(startCol == destCol) {
      if(this.getColor() == true) {
        if((startRow+ 1) == destRow) {
          return true;
        }
      }
      else {
        if((startRow - 1) == destRow) {
          return true;
        }
      }
      return false;
    }
    return false;
  }

  // return the moves as a 1D array with location of the form col1, row1, col2, row2 and so on
  // for pawns it does not really matter, all we need to check are the straight and diagonal moves
  public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore){
    int[] validMoves = new int[0];

    int[] movesRow = getAttackRow();
    int[] movesCol = getAttackCol();
    boolean isValidAttack = false;
    boolean isValidMove = false;

    for(int i = 0; i < 2; i++) {
      if((startRow + movesRow[i] == destRow && startCol + movesCol[i] == destCol)) {
        isValidAttack = true;
      }
    }

    isValidMove = checkStraightMove(startRow, startCol, destRow, destCol);

    if(!isValidAttack && !isValidMove) {
      if(ignore == true) {
        return new int[0];
      }
      return null;
    }
    return validMoves;
  }

  // Boolean function that determines if self (which is a pawn) is attacking another chesspiece at row and col, given as argument
  // Input: the chesspiece with whom we want to check the attack
  // Output: True if self is attacking the chesspiece at position row and col, false otherwise
  public boolean isAttacking(ChessPiece piece)
  {
      int[] attackRow = getAttackRow();
      int[] attackCol = getAttackCol();

      for(int i = 0; i < 2; i++) {
        if((this.getRow() + attackRow[i] == piece.getRow() && this.getCol() + attackCol[i] == piece.getCol()) && (this.getColor() != piece.getColor())) {
          return true;
        }
      }
      return false;
  }
}

// End
