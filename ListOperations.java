// Filename: ListOperations.java
//
// Contains class ListOpertaions that support some more utility functions related to operations on list
//
// This needs to be compiled with ChessPiece.java, Node.java, Utilities.java, King.java, Queen.java, Knight.java, Rook.java, Bishop.java and Pawn.java.
//
// Santrupti Nerli, Feb 2017

import java.io.*;

class ListOperations {

  // Method to perform insertion at the front of the list
  // Input: Node to be inserted and the list to insert it into
  // Output: return the list where you just inserted the node
  public static Node insert(Node list, Node piece) {
    Node temp = list.getNext();
    list.setNext(piece);
    piece.setNext(temp);
    return list;
  }

  // Method to find Node in a given location
  // Input: integer row and column to look for along with the list
  // Output: Node found
  public static Node findChessPiece(Node list, int row, int col) {
    Node piece = list.getNext();
    while(piece != null) {
      if(piece.getRow() == row && piece.getCol() == col) {
        return piece;
      }
      piece = piece.getNext();
    }
    return null;
  }

  // Method to find Node based on the type
  // Input: char type and the list
  // Output: Node found
  public static Node findChessPiece(Node list, char pieceType) {
    Node piece = list.getNext();
    while(piece != null) {
      if(Utilities.returnChessPieceType(piece) == pieceType) {
        return piece;
      }
      piece = piece.getNext();
    }
    return null;
  }

  // Method to check if two nodes given are different or the same ones
  // It serves as a helper when trying to find the attack
  // Input: two nodes
  // Output: returns if they are same or different pieces
  public static boolean isDifferent(Node one, Node other) {
    if(one.getRow() == other.getRow() && one.getCol() == other.getCol() && one.getColor() == other.getColor()) {
      return false;
    }
    return true;
  }

  // Method to perform deletion of a node in a list.
  // First, it finds a node that contains piece to capture and deletes it
  // Input: Node containing piece to delete
  // Output: returns the modified list
  public static Node deleteNode(Node list, Node pieceToCapture) {
    Node piece = list.getNext();
    Node previous = list;

    while(piece != null) {
      if(!isDifferent(piece, pieceToCapture)) {
        previous.setNext(previous.getNext().getNext());
        return list;
      }
      previous = piece;
      piece = piece.getNext();
    }
    return list;
  }

  // Method to perform copy of the list
  // Input: node for which we want to return a copy
  // Output: returns the copy of the list
  public static Node listCopy(Node head) {
    Node headCopy = new Node();
    Node headCopyPiece = headCopy;
    Node piece = head.getNext();
    while(piece != null) {
      headCopyPiece.setNext(new Node(Utilities.returnChessPieceType(piece), piece.getRow(), piece.getCol()));
      headCopyPiece = headCopyPiece.getNext();
      piece = piece.getNext();
    }
    return headCopy;
  }

  // Additional methods to check validity of the board

  // Method to count the number of chesspieces for a given type
  // This method will helps us check the validity case
  // Input: character color and the list
  // Output: returns the count
  public static int countPiecesOfType(Node head, char pieceType) {
    Node piece = head.getNext();
    int pieceCtr = 0;
    // loop through to check if the same piece type is found
    while(piece != null) {
      if(Utilities.returnChessPieceType(piece) == pieceType) {
        pieceCtr++;
      }
      piece = piece.getNext();
    }
    return pieceCtr;
  }

  // Method to count the number of chesspieces on a single location
  // This method will helps us check the validity case
  // Input: integer row and column and the list
  // Output: returns the count
  public static int countPiecesInLocation(Node head, int row, int col) {
    Node piece = head.getNext();
    int pieceCtr = 0;
    // loop through to check if any two pieces overlap
    while(piece != null) {
      if(piece.getRow() == row && piece.getCol() == col) {
        pieceCtr++;
      }
      piece = piece.getNext();
    }
    return pieceCtr;
  }

  // Method to check if two pieces occupy the same place
  // This method utilizes the countPiecesInLocation method to see
  // if there are more than two pieces in a single location
  // Input: list
  // Output: returns true if two pieces occupy same position
  public static boolean twoPiecesOccupySamePosition(Node head) {
    Node piece = head.getNext();
    // loop through and see if any of the pieces overlap
    while(piece != null) {
      if(countPiecesInLocation(head, piece.getRow(), piece.getCol()) > 1) {
        return true;
      }
      piece = piece.getNext();
    }
    return false;
  }

  // Method to check validity
  // basically looks if there are not two chesspieces in the same location and
  // one each colored king is present
  // Input: list
  // Output: returns if it is valid or not
  public static boolean checkValidity(Node head) {
    if(!twoPiecesOccupySamePosition(head) && countPiecesOfType(head, 'k') == 1 && countPiecesOfType(head, 'K') == 1) {
      return true;
    }
    else {
      return false;
    }
  }
}

// end
