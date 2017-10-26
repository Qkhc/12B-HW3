// Filename: Utilities.java
//
// Contains class Utilities that provides some utility functions
//
// Santrupti Nerli, Jan 2017

class Utilities {

  // Convenient method to print error message and exit
  // Input: String message to be printed
  // Output: void, simply exits program
  public static void errExit(String message)
  {
      System.err.println(message);
      System.exit(1);
  }

  // Convenient method to print the list at any point of time
  // It can be used to check if list is created properly or not
  // Input: list that needs to be printed
  // Output: void, simply prints the list
  public static void printList(Node head) {
    // since first node is head, I want to move to the next pointer of head and not head itself
    Node myPiece = head.getNext();
    // loop through each node and print its contents
    while(myPiece != null) {
      System.out.println("Row: " + myPiece.getRow() + " Col: " + myPiece.getCol() + " Color: " + myPiece.getColor());
      myPiece = myPiece.getNext();
    }
  }

  // A cool method that actually prints a chessboard with chesspieces onto the console. Uses some UNICODE awesomeness
  // Input: 2D character array isFilled, where isFilled[i][j] is K, Q, R, B or N for black king, queen, rook, bishop or knight chesspieces and
  // k, q, r, b or n if there are white king, queen, rook, bishop or knight respectively at row i, col j.
  // Assumes that isFilled is board_size X board_size.
  // Output: void, basically prints the chessboard with the chesspiece onto console
  public static void printSolution(char[][] isFilled, int board_size){
      String line = "  "; // starting strings to be printed. line is the dividing horizontal strip along the board
      String col_nums = "  ", col_str = ""; // these strings are for printing the column numbers below

      for (int i=1; i < board_size+1; i++) { // loop over all the columns to create the line and col_nums strings
          line = line + "+--"; // each iteratin add "+--" to line
          if (i < 10) // have to break into cases, since i < 10 is one symbol, but i >= 10 is two
              col_str = " " + Integer.toString(i) + " ";  // add string i to col_str with spacing
          else
              col_str = " " + Integer.toString(i);  // add string i to col_str with less spacing, since i >= 10
          col_nums = col_nums + col_str ; // append col_str to col_nums
      }
      line = line + "+";  // complete line string

      for (int i=board_size; i > 0; i--) { // loop over all the rows in decreasing order. each iteration will print a col
          System.out.println(line); // start by printing a line
          String pieces, background, chesspiece; // pieces string will actually put the chesspiece symbols into string
          if (i < 10)   // pieces begins with col number, again break into cases if i has 1 vs 2 symbols
              pieces = " "+Integer.toString(i);
          else
              pieces = Integer.toString(i);

          for (int j=1; j < board_size+1; j++) { // now loop over cols to create individual squares
              if ((i+j)%2 == 1) // place alternating black or red background for squares
                  background = "\u001B[40m";  // ANSI escape code for black
              switch(isFilled[i][j]) {
                  case 'k':
                      chesspiece = "k "; // put UNICODE symbol for white king
                      break;
                  case 'q':
                      chesspiece = "q "; // put UNICODE symbol for white queen
                      break;
                  case 'r':
                      chesspiece = "r "; // put UNICODE symbol for white rook
                      break;
                  case 'b':
                      chesspiece = "b "; // put UNICODE symbol for white bishop
                      break;
                  case 'n':
                      chesspiece = "n "; // put UNICODE symbol for white knight
                      break;
                  case 'p':
                      chesspiece = "p "; // put UNICODE symbol for white knight
                      break;
                  case 'K':
                      chesspiece = "K "; // put UNICODE symbol for black king
                      break;
                  case 'Q':
                      chesspiece = "Q "; // put UNICODE symbol for black queen
                      break;
                  case 'R':
                      chesspiece = "R "; // put UNICODE symbol for black rook
                      break;
                  case 'B':
                      chesspiece = "B "; // put UNICODE symbol for black bishop
                      break;
                  case 'N':
                      chesspiece = "N "; // put UNICODE symbol for black knight
                      break;
                  case 'P':
                      chesspiece = "P "; // put UNICODE symbol for black pawn
                      break;
                  default:
                      chesspiece = "  ";
              }

              pieces = pieces + "|"+chesspiece+"\u001B[0m"; // put UNICODE symbol for queen with a line. also set background, and then apply ANSI reset code
          }
          System.out.println(pieces+"|");
      }

      System.out.println(line); // print out the final line
      System.out.println(col_nums); // print out the columns below
  }

  // Method to return a charceter based on the chesspiece type
  // Input: Node to determine its type
  // Output: returns char based on chesspiece type and its color
  // It is useful for printing into files and checking validity
  public static char returnChessPieceType(Node node) {
      ChessPiece piece = node.getChessPiece();
      if(piece instanceof King) {
        if(piece.getColor() == true) {
            return 'k';
        }
        return 'K';
      }
      else if(piece instanceof Queen) {
        if(piece.getColor() == true) {
            return 'q';
        }
        return 'Q';
      }
      else if(piece instanceof Rook) {
        if(piece.getColor() == true) {
            return 'r';
        }
        return 'R';
      }
      else if(piece instanceof Bishop) {
        if(piece.getColor() == true) {
            return 'b';
        }
        return 'B';
      }
      if(piece instanceof Knight) {
        if(piece.getColor() == true) {
            return 'n';
        }
        return 'N';
      }
      if(piece instanceof Pawn) {
        if(piece.getColor() == true) {
            return 'p';
        }
        return 'P';
      }
      return '-';
  }

  // Method to iterate through the list and update a 2D array for printing the board
  // onto the console
  // Input: integer board number read from input.txt, board size and the list
  // Output: void, just print the solution
  public static void convertFromListToMatrixAndPrint(Node list, int board_no, int board_size) {
    // Initialize isFilled board
    char[][] isFilled = new char[board_size+1][board_size+1];
    for(int i = 0; i < board_size; i++) {
      for(int j = 0; j < board_size; j++) {
        isFilled[i][j] = '-';
      }
    }
    // iterate through the list and update isFilled matrix
    Node piece = list.getNext();
    while(piece != null) {
      isFilled[piece.getRow()][piece.getCol()] = Utilities.returnChessPieceType(piece);
      piece = piece.getNext();
    }

    System.out.println("Board No: " + (board_no));
    Utilities.printSolution(isFilled, board_size);
  }

}

// end
