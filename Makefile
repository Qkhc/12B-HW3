ChessPuzzle.jar: ChessPuzzle.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class
	echo Main-class: ChessPuzzle > Manifest
	jar cvfm ChessPuzzle.jar Manifest ChessPuzzle.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class
	rm Manifest

ChessPuzzle.class Node.class ListOperations.class Utilities.class ChessPiece.class King.class Queen.class Rook.class Bishop.class Knight.class Pawn.class: ChessPuzzle.java Node.java ListOperations.java Utilities.java ChessPiece.java King.java Queen.java Rook.java Bishop.java Knight.java Pawn.java
	javac -Xlint *.java

clean:
	rm *.class
	rm *.jar
