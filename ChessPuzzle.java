// Filename: ChessBoard.java
//
// Contains class ChessBoard that creates a linkedlist of chessboard where each node holds a chesspiece to be placed
//
// This needs to be compiled with ChessPiece.java, Node.java, Utilities.java, King.java, Queen.java, Knight.java, Rook.java, Bishop.java and Pawn.java.
//
// Output:
//     - analysis.txt file is created corresponding board and query entry in input.txt
//
// Santrupti Nerli, Jan 2017
//
import java.io.*;

class ChessPuzzle {

  private static Node head; // linkedlist to store chesspieces
  private static int board_size; // board_size
  private static int board_no; // current board no, we are processing
  private static int moves; //number of to find checkmate
  public static BufferedWriter writer; // write to write to file
  public static final int possibleRowMoves[] = {-1, -1, 0, 1, 0, 1, 1, -1}; // these are the possible row moves for a king
  public static final int possibleColMoves[] = {0, -1, -1, -1, 1, 1, 0, 1}; // these are the possible col moves for a king

  // constructor
  public ChessPuzzle() {
    head = new Node();
  }

  // Method to perform capture.
  // First, it finds a node that contains piece to capture and captures it
  // Input: Node containing piece to capture
  // Output: returns the modified list
  public Node capture(Node list, Node pieceToCapture) {
    return ListOperations.deleteNode(list, pieceToCapture);
  }

  // Method to check if given coordinates are out of the board or not
  // Input: row and column
  // Output: returns true if the coordinates lie outside the board
  public boolean isOutOfBoard(int row, int col) {
    if(row <= 0 || col <= 0 || row > board_size || col > board_size) {
      return true;
    }
    return false;
  }

  // Method to place the chesspiece in a final destination
  // if there is a piece of other color in the destination, it performs capture as well
  // Input: Node containing piece to move, node containing piece in destination, destination location
  // Output: returns if placement is successful or not
  public boolean placePiece(Node list, Node piece, Node dest, int row, int col, boolean shouldPrint) {
    if(dest != null) {
      if(piece.getColor() != dest.getColor()) {
        list = capture(list, dest);
      }
      else {
        if(shouldPrint) {
          System.out.println("Invalid Move from " + piece.getCol() + " " + piece.getRow() +
          " to " + row + " " + col + ": Destination piece, " + Utilities.returnChessPieceType(dest)
          + " is the same color as that of " + Utilities.returnChessPieceType(piece));
        }
        return false;
      }
    }
    piece.setRow(row);
    piece.setCol(col);
    return true;
  }

  // Method to check if the move is blocked
  // if there is a piece in the path to destination, it returns false
  // Input: all the valid moves of a piece in the direction of destination
  // Output: returns if a move is blocked or not
  public boolean checkBlock(Node list, int[] validMoves, int startRow, int startCol, int destRow, int destCol, boolean shouldPrint) {
    for(int j = 0; j < validMoves.length; j += 2) {
      Node piece = ListOperations.findChessPiece(list, validMoves[j+1], validMoves[j]);
      if(piece != null) {
        if(shouldPrint) {
          System.out.println("Invalid Move from " + startCol + " " + startRow + " to " + destCol +
          " " + destRow + ": Blocked by " + Utilities.returnChessPieceType(piece) + " at position "
          + validMoves[j] + " " + validMoves[j+1]);
        }
        return false;
      }
    }
    return true;
  }

  // Method to move pawns if the positions given are valid
  // If it is a straight move, then all you have to check is if it is one step move and that there
  // are no other pieces in destination. If it is not a straight move, check if it is
  // any of the immediate left or right and that there is a piece of different color.
  // Input: pieceToMove, piece in the destination, starting column and new column to move to
  // Output: returns if a move is successful or not
  public boolean movePawns(Node list, Node pieceToMove, Node pieceInDestination,
                          int newRowToMoveTo, int newColToMoveTo, boolean shouldPrint) {
    // check if it a straight move
    if(pieceToMove.getCol() == newColToMoveTo) {
      // check if the move is just by one row
      if((pieceToMove.getRow() + 1) == newRowToMoveTo || (pieceToMove.getRow() - 1) == newRowToMoveTo) {
        // check if it is not blocked
        if(pieceInDestination == null) {
          pieceToMove.setCol(newColToMoveTo);
          pieceToMove.setRow(newRowToMoveTo);
          return true;
        }
        else {
          // If someone is blocking the pawn
          if(shouldPrint) {
            System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) + " at "
            + pieceToMove.getCol() + " " + pieceToMove.getRow() + " is blocked by " +
            Utilities.returnChessPieceType(pieceInDestination) + " at " + newColToMoveTo + " " + newRowToMoveTo);
          }
          return false;
        }
      }
    }
    else {
      // if the piece in destination is not null, check if it lies in diagonals and place piece
      // only if you can capture. Otherwise just print that it is not possible and return false
      if(pieceInDestination != null) {
        return placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
      }
      else {
        if(shouldPrint) {
          System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) +
          " cannot move from " + pieceToMove.getCol() + " " + pieceToMove.getRow() + " to " + newColToMoveTo +
          " " + newRowToMoveTo);
        }
        return false;
      }
    }
    return true;
  }

  // Method to perform moves based on the query read from input.txt
  // It has 3 cases, one for knight, one for pawn and another one for the rest of the chess pieces
  // Input: query. Basically, the move positions
  // Output: returns the vector with bits that are valid (or moves possible) set to true
  public boolean[] makeMoves(Node list, int[] query, boolean shouldPrint) {

    // moveslog will keep track of valid moves until you hit invalid
    // so we are allocating the maximum no. of valids in any given query
    boolean[] movesLog = new boolean[query.length/4];
    // for each move in the query perform the following
    for(int i = 0; i < query.length; i += 4) {

      // keep the start and dest locations and current move you are performing
      int startCol = query[i];
      int startRow = query[i+1];
      int newColToMoveTo = query[i+2];
      int newRowToMoveTo = query[i+3];
      int currentQuery = (i/4);

      // see who is in the start position
      Node pieceToMove = ListOperations.findChessPiece(list, startRow, startCol);
      // if no piece exists in the given starting position, then it is invalid
      // so just stop further processing
      if(pieceToMove == null) {
        if(shouldPrint) {
            System.out.println("Invalid Move: No piece present at " + startCol + " " + startRow);
        }
        movesLog[currentQuery] = false;
        break;
      }
      // see who is in destination
      Node pieceInDestination = ListOperations.findChessPiece(list, newRowToMoveTo, newColToMoveTo);
      char pieceType = Utilities.returnChessPieceType(pieceToMove);

      // get the moves for the start piece
      int[] validMoves = pieceToMove.getChessPiece().getMoves(startRow, startCol, newRowToMoveTo, newColToMoveTo, false);
      // check if the given move is valid or not
      if(validMoves == null) {
        if(shouldPrint) {
            System.out.println("Invalid Move: " + Utilities.returnChessPieceType(pieceToMove) + " cannot move from "
            + startCol + " " + startRow + " to " + newColToMoveTo + " " + newRowToMoveTo);
        }
        movesLog[currentQuery] = false;
        break;
      }

      // initialize it to false
      movesLog[currentQuery] = false;
      // For Knight
      if(pieceType == 'n' || pieceType == 'N') {
        movesLog[currentQuery] = placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
      }
      // For Pawn
      else if(pieceType == 'p' || pieceType == 'P') {
        movesLog[currentQuery] = movePawns(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
      }
      // For the other pieces. Check the blocking and then place piece if not blocked
      else {
        Node piece = list.getNext();
        movesLog[currentQuery] = checkBlock(list, validMoves, startRow, startCol, newRowToMoveTo, newColToMoveTo, shouldPrint);
        if(movesLog[currentQuery]) {
          movesLog[currentQuery] = placePiece(list, pieceToMove, pieceInDestination, newRowToMoveTo, newColToMoveTo, shouldPrint);
        }
      }
      if(movesLog[currentQuery] == false) {
        break;
      }
      if(shouldPrint) {
        System.out.println("Board after performing the move: " + startCol + " " + startRow + " to " +
        newColToMoveTo + " " + newRowToMoveTo);
        Utilities.convertFromListToMatrixAndPrint(list, board_no, board_size);
      }
    }
    return movesLog;
  }

  // variant of makeMove that only makes a single move, and verifies validity. Thus, player cannot be in check after move.
  // Input: chessboard list, and single move, which is an int array of length 4
  // Output: boolean whether move is valid. If move is possible, the move is performed on list. If move is not possible, list does not change
  
  public boolean makeValidMove(Node list, int[] move)  {

    if (move.length != 4) // move should only have 4 ints
        Utilities.errExit("Incorrect argument to makeValidMove");
    
    Node toMove = ListOperations.findChessPiece(list,move[1],move[0]); // determine piece at source square
    ChessPiece piece = toMove.getChessPiece();

    if (piece == null) // if it doesn't exist, return false
        return false;
    boolean player = piece.getColor(); // find the color of the player

    if (determineCheck(list,!player)) // if opponent in check, then player shouldn't be moving. This is probably checkmate
        return false;

    Node copy = ListOperations.listCopy(list); // just to be careful, make moves on a copy
    boolean[] moveOutput = makeMoves(copy, move, false);
    boolean possible = moveOutput[0];  // now call makeMoves on the copy

    if (!possible)   // move isn't possible, so return false
        return false;

    if (determineCheck(copy,player)) // player cannot be in check after move, so move is invalid
        return false;

    // this move must be valid
    return (makeMoves(list,move,false))[0]; // now, it is safe to perform move on list

  }

  // Input: color of the king
  // Output: returns the corresponding king node
  public Node getKingNode(Node list, boolean kingColor) {
    Node kingNode  = null;
    if(kingColor == true) {
      kingNode = ListOperations.findChessPiece(list, 'k');
    }
    else {
      kingNode = ListOperations.findChessPiece(list, 'K');
    }
    return kingNode;
  }

  // Method to check if a king is in check
  // Input: color of the king
  // Output: returns if there is a check or not
  public boolean determineCheck(Node list, boolean kingColor) {
    // get the first valid chesspiece (remember not the head)
    Node piece = list.getNext();
    Node king  = getKingNode(list, kingColor);
    int row = king.getRow();
    int col = king.getCol();

    // loop through each of the remaining chesspieces and check for attack
    while(piece != null) {
      // if opposite knight gives a check. Then none of them can block. Because knight can jump
      if(ListOperations.isDifferent(piece, king) && piece.getChessPiece().isAttacking(king.getChessPiece())) {
        if(Utilities.returnChessPieceType(piece) == 'n' || Utilities.returnChessPieceType(piece) == 'N') {
          return true;
        }
        else {
          // for others, we need to see if someone is blocking for an opposite piece to give a check
          int[] validMoves = piece.getChessPiece().getMoves(piece.getRow(), piece.getCol(), row, col, true);
          if(checkBlock(list, validMoves, piece.getRow(), piece.getCol(), row, col, false)) {
            return true;
          }
        }
      }
      piece = piece.getNext();
    }
    return false;
  }

  // Method to check if a king is in weak checkmate
  // Input: color of the king
  // Output: returns if there is a weak checkmate or not
  public boolean determineWeakCheckmate(Node head, boolean kingColor) {
    Node list = ListOperations.listCopy(head);
    boolean checkmate = false;
    // 8 surrounding moves. So a total of 8 positions to check. Let
    // us start by assuming that the king can move to all 8 surrounding positions
    int movePossibility[] = {1, 1, 1, 1, 1, 1, 1, 1};
    int attackCtr = 0;
    int spaceCtr = 0;

    // get the king node, store the locations for accessibility
    Node king = getKingNode(list, kingColor);
    char type = Utilities.returnChessPieceType(king);
    int row = king.getRow();
    int col = king.getCol();

    // proceed to verify the checkmate only if the given king is under check
    if(!determineCheck(list, kingColor)) {
      return false;
    }

    // for all possible moves around that king
    for(int i =0; i < possibleRowMoves.length; i++) {
      // avoid possible moves out of the board
      if(isOutOfBoard(row+possibleRowMoves[i], col+possibleColMoves[i])) {
        movePossibility[i] = 0;
        continue;
      }
      // move the king to his adjacent position
      int[] query = {col, row, col+possibleColMoves[i], row+possibleRowMoves[i]};
      Node newList = ListOperations.listCopy(list);
      boolean[] newPossibility = makeMoves(newList, query, false);

      // perform the move (or not is there is a same colored piece in the adjacent location),
      // then determine if he is still under check
      // if so, then that move/location does not prevent the checkmate
      if(determineCheck(newList, kingColor)) {
        movePossibility[i] = 0;
      }
    }
    // if all the possible places for that king are occupied, then he is under checkmate
    for(int i = 0; i < movePossibility.length; i++) {
      if(movePossibility[i] == 0) {
        attackCtr++;
      }
    }
    if(attackCtr == movePossibility.length) {
      checkmate = true;
    }
    return checkmate;
  }

  // Method to check if a king is in real checkmate
  // Input: color of the king
  // Output: returns if there is a real checkmate or not
  public boolean determineRealCheckmate(Node head, boolean kingColor) {
    // create a copy of the original list
    Node list = ListOperations.listCopy(head);
    // fetch the king node
    Node kingNode = getKingNode(list, kingColor);

    boolean isCheckmate = false;
    
    // if the given king is under weak chckmate, then we want to
    // place all possible pieces of same color that can block the checkmate
    // given that the move for the piece is valid
    if(determineWeakCheckmate(list, kingColor)) {

        isCheckmate = true;   // assume checkmate is possible, since weak checkmate is possible

        // loop over all squares to find the pieces
        for (int col=1; col <= board_size; col++)
        {
            for (int row=1; row <= board_size; row++)
            {
                Node nodeSrc = ListOperations.findChessPiece(list,row,col);
                if (nodeSrc == null) // no piece at src square
                    continue;

                ChessPiece atSrc = nodeSrc.getChessPiece(); // get the chesspiece at the src square
                if (atSrc.getColor() != kingColor) // either no piece at src square or if piece there is not of starting color, then continue to the next square
                    continue;

                for (int destCol=1; destCol <= board_size; destCol++)
                {
                    for (int destRow=1; destRow <= board_size; destRow++)
                    {
                        Node copy = ListOperations.listCopy(list);
                        int[] nextTry = new int[4];
                        nextTry[0] = col;
                        nextTry[1] = row;
                        nextTry[2] = destCol;
                        nextTry[3] = destRow;
    
                        char pieceType = Utilities.returnChessPieceType(nodeSrc);
                        boolean possible = makeValidMove(copy,nextTry); // is this a valid move
                        if (possible)  // if move is valid, player can escape check
                        {
                            isCheckmate = false;
                            break;
                        }
                    }
                    if (!isCheckmate)
                        break;
                }
                if (!isCheckmate)
                    break;
            }
            if (!isCheckmate)
                break;
        }
    }
    return isCheckmate;
  }

  // Method to write to the analysis.txt file
  // Input: String to write
  // Output: void, just write
  public void writeToAnalysisFile(String stringToWrite) {
    try {
        writer.write(stringToWrite);
    }
    catch (Exception e) {
        Utilities.errExit("Exception occurred while trying to write to file:"
                          + "writeToAnalysisFile"); // throw a generic exception if failure to read occurs
    }
  }

  // Method to read from input.txt
  // for each chessboard and query, perform all the required operations
  // an then proceed further
  // Input: none
  // Output: void, jusr read and perform requested operations
  public static void readFromInputFile() {

    int lineCtr = 0;
    int[] query;
    ChessPuzzle c = null;

    try {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] args = line.split(" "); // Reader assumes that the input format is as given in the instruction
            // If the line is 2i, then I know that it is a configuration of a ChessBoard
            // so create a new ChessBoard here, parse board size and insert
            // given chesspieces into the linked list
            c = new ChessPuzzle();
            board_no++;
            moves = Integer.parseInt(args[0].substring(0,1));
            board_size = 8;
            for(int i = 1; i < args.length; i += 3) {
              head = ListOperations.insert(head, new Node(args[i].charAt(0), Integer.parseInt(args[i+2]), Integer.parseInt(args[i+1])));
            }
            performOperations(c);
        }
        reader.close();
    }
    catch (NumberFormatException e) {
        Utilities.errExit("All arguments must be integers"); // throw error incase parsing integer fails
    }
    catch (IndexOutOfBoundsException e) {
        Utilities.errExit("Array index is out of bounds"); // throw error when inserting elements into arrays fail
    }
    catch (Exception e) {
        Utilities.errExit("Exception occurred trying to read file"); // throw a generic exception if failure to read occurs
    }
  }
  
  // Method to determine if checkmate is possible for white
  // in the given number of moves
  // Input: Chessboard and the moves
  // Output: returns true if black is checkmated
    public String attemptCheckmateWhite(Node list, int moves){
        if(moves == 0){
            if(determineRealCheckmate(list, false) || determineRealCheckmate(list,true)){
                return "Solution";
            }
            return "No solution";
        }
        else if(moves == this.moves){
           Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateWhite(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    // print the final board after performing all the moves
                                    System.out.println("Board after performing all the valid moves");
                                    Utilities.convertFromListToMatrixAndPrint(list, board_no, board_size);
                                    System.out.println();
                                    return type+" "+col+" "+row+" "+i+" "+j;
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
        else if(moves == 1){
           Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateWhite(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    return type+" "+col+" "+row+" "+i+" "+j;
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
        else if(moves%2 == 0){
            Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(!current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(attemptCheckmateWhite(copy, moves-1).equals("No solution"))
                                    return "No solution";
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "Checkmate";
        }
        else{
          Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateWhite(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    return type+" "+col+" "+row+" "+i+" "+j;
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
  }
    
  // Method to determine if checkmate is possible for black
  // in the given number of moves
  // Input: Chessboard and the moves
  // Output: returns true if black is checkmated
    public String attemptCheckmateBlack(Node list, int moves){
        if(moves == 0){
            if(determineRealCheckmate(list, false) || determineRealCheckmate(list,true)){
                return "Solution";
            }
            return "No solution";
        }
        else if(moves == this.moves){
           Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateBlack(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    // print the final board after performing all the moves
                                    System.out.println("Board after performing all the valid moves");
                                    Utilities.convertFromListToMatrixAndPrint(list, board_no, board_size);
                                    System.out.println();
                                    return "Black can win";
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
        else if(moves == 1){
           Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(!current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateBlack(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    return "Black can win";
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
        else if(moves%2 == 0){
            Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(attemptCheckmateBlack(copy, moves-1).equals("No solution"))
                                    return "No Solution";
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "Black can win";
        }
        else{
          Node current = list.getNext();
            while(current != null){
                int col = current.getCol();
                int row = current.getRow();
                char type = current.getPieceType();
                if(!current.getColor()){
                    for(int i=1; i<=8; i++){
                        for(int j=1; j<=8; j++){
                            Node copy = ListOperations.listCopy(list);
                            int[] attemptMove = {col,row,i,j};
                            if(makeValidMove(copy,attemptMove)){
                                if(!attemptCheckmateBlack(copy, moves-1).equals("No solution")){
                                    current.setCol(i);
                                    current.setRow(j);
                                    return "Black can win";
                                }
                            }
                        }
                    }
                }
                current =current.getNext(); 
            }
            return "No solution";
        }
  }
  
  // Method to perform all the requested operations
  // namely, check validity, identify check,
  // identify strong and weak checkmates and perform moves
  // Input: ChessBoard and the query
  // Output: returns the count
  public static void performOperations(ChessPuzzle c) {
    try {
      // check the king checks and weak and strong checkmates
      boolean isWhiteKingUnderAttack = c.determineCheck(head, true);
      boolean isBlackKingUnderAttack = c.determineCheck(head, false);
      boolean weakCheckmateWhite = c.determineWeakCheckmate(head, true);
      boolean weakCheckmateBlack = c.determineWeakCheckmate(head, false);
      boolean realCheckmateWhite = c.determineRealCheckmate(head, true);
      boolean realCheckmateBlack = c.determineRealCheckmate(head, false);

      // print the initial board
      System.out.println("Initial Board");
      Utilities.convertFromListToMatrixAndPrint(head, board_no, board_size);
      
      //check if puzzle is valid
      if(!ListOperations.checkValidity(head)){
          System.out.println("No solution");
          c.writeToAnalysisFile("No solution");
          writer.newLine();
      }
      else{
        //attempt checkmate in given moves
        String output;
        if(moves%2 ==1)
            output = c.attemptCheckmateWhite(head,moves);
        else
            output = c.attemptCheckmateBlack(head,moves);
        System.out.println(output);
        c.writeToAnalysisFile(output);
        writer.newLine();
      }
      
      for(int k = 0; k < 50; k++) {
        System.out.print('-');
      }
      System.out.println();
    }
    catch(Exception e) {
      Utilities.errExit("Error while performing operations");
    }

  }

  // main method
  public static void main(String[] args) {
    try{
      writer = new BufferedWriter(new FileWriter("solution.txt")); // open the file to write
      readFromInputFile(); // read from input file and perform operations
      writer.close(); // close the writer
    }
    catch(Exception e) {
      Utilities.errExit("Error while creating BufferedWriter");
    }

  }
}

// End
