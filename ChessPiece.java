// Filename: ChessPiece.java
//
// Contains the class ChessPiece that represents a chesspiece
// which is the super class for all the chesspieces
//
// Santrupti Nerli, Jan 2017

class ChessPiece {

  private int row; // row where the chesspiece is present
  private int col; // col where the chesspiece is present
  private boolean color; // color of the chesspiece

  // constructor without any args
  public ChessPiece() {
    this.row = -1;
    this.col = -1;
    this.color = false;
  }

  // constructor with args
  public ChessPiece(int row, int col, boolean color) {
    this.row = row;
    this.col = col;
    this.color = color;
  }

  // More like a copy constructor
  public ChessPiece(ChessPiece piece) {
    this.row = piece.row;
    this.col = piece.col;
    this.color = piece.color;
  }

  // return the row of the current chesspiece
  public int getRow() {
    return this.row;
  }

  // return the col of the current chesspiece
  public int getCol() {
    return this.col;
  }

  // return the color of the current chesspiece
  public boolean getColor() {
    return this.color;
  }

  // set the col of the current chesspiece
  public void setCol(int col) {
    this.col = col;
  }

  // return the color of the current chesspiece
  public void setRow(int row) {
    this.row = row;
  }

  // just a dummy method, all the children override it
  public int ifValidReturnLength(int startRow, int startCol, int destRow, int destCol) {
    return 0;
  }

  // just a dummy method, all the children override it
  public int[] getMoves(int startRow, int startCol, int destRow, int destCol, boolean ignore) {
    int[] validMoves = new int[0];
    return validMoves;
  }

  // Dummy method to check attack
  // It will be overridden by each of the child classes that inherit ChessPiece
  // Input: integer row and column to look for
  // Output: boolean which returns false (dummy) at all point of time
  public boolean isAttacking(ChessPiece piece) {
    System.out.println("Should never come here");
    // Do nothing. Just return false for everything
    return false;
  }
}

// End
