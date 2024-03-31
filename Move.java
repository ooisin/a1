package Checkers;

import static processing.core.PApplet.dist;
import static processing.core.PApplet.lerp;

public class Move {
        public static boolean isValidMove(Cell originCell, Cell targetCell) {

            if (targetCell.getPiece() != null) {
                return false;
            }

            int colDiff = targetCell.getX() - originCell.getX();
            int rowDiff = targetCell.getY() - originCell.getY();


            // Movement checks (either move one square diagonally or jump over two)
            if (!((Math.abs(colDiff) == 1 && Math.abs(rowDiff) == 1) || (Math.abs(colDiff) == 2 && Math.abs(rowDiff) == 2))) {
                return false;
            }

            // Player one piece (b or (need to add for king) B)
            if (App.getPlayer() == 'b') {
                if (!(rowDiff == -1 || rowDiff == -2)) {// changed X and Y
                        return false;
                }

                if (Math.abs(colDiff) == 2) {
                    // Check for a piece to jump over
                    int midCol = (originCell.getX() + targetCell.getX()) / 2;
                    int midRow = (originCell.getY() + targetCell.getY()) / 2;
                    Cell midPiece = new Cell(midRow, midCol);


                    if (midPiece.getPiece() == null) {
                        return false; // No piece to jump over
                    }
                    // Capture: Clear the jumped piece for actual game move; for validation, this
                    // might be skipped or handled elsewhere

                }
                return true; // Move is valid
            }

            // Player two piece (w or W)
            if (App.getPlayer() == 'w') {

                    // w can only move forward (colDiff 1 for normal, 2 for capture)
                    if (!(rowDiff == 1 || rowDiff == 2)){
                        return false;
                }

                if (Math.abs(colDiff) == 2) {
                    // Check for a piece to jump over
                    int midCol = (originCell.getX() + targetCell.getX()) / 2;
                    int midRow = (originCell.getY() + targetCell.getY())/ 2;
                    Cell midPiece = new Cell(midRow, midCol);

                    if (midPiece.getPiece() == null) {
                        return false; // No piece to jump over
                    }
                    // Capture: Clear the jumped piece for actual game move; for validation, this
                    // might be skipped or handled elsewhere
                    return true; // Move is valid
                }
            }

            return false; // If none of the conditions are met, the move is invalid
        }


        public static void processMove(Cell originCell, Cell targetCell) {

            targetCell.setPiece(originCell.getPiece());
            originCell.setPiece(null);


        }

}