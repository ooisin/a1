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

                if (rowDiff == -2) {
                    // Check for a piece to jump over
                    int deltaX = targetCell.getX() - originCell.getX();
                    int deltaY = targetCell.getY() - originCell.getY();

                    int midCol = originCell.getX() + deltaX / 2;
                    int midRow = originCell.getY() + deltaY / 2;

                    Cell midCell = App.getBoard()[midRow][midCol];
                    CheckersPiece midPiece = midCell.getPiece();
                    if (midPiece == null) { // if there is no piece in the middle you cant move 2 squares
                        return false; // No piece to jump over
                    }

                }
                return true; // Move is valid
            }

            // Player two piece (w or W)
            if (App.getPlayer() == 'w') {
                if (!(rowDiff == 1 || rowDiff == 2)) {// changed X and Y
                    return false;
                }

                if (rowDiff == 2) {
                    // Check for a piece to jump over
                    int deltaX = targetCell.getX() - originCell.getX();
                    int deltaY = targetCell.getY() - originCell.getY();

                    int midCol = originCell.getX() + deltaX / 2;
                    int midRow = originCell.getY() + deltaY / 2;

                    Cell midCell = App.getBoard()[midRow][midCol];
                    CheckersPiece midPiece = midCell.getPiece();
                    if (midPiece == null) { // if there is no piece in the middle you cant move 2 squares
                        return false; // No piece to jump over
                    }

                }
                return true; // Move is valid
            }

            return false; // If none of the conditions are met, the move is invalid
        }


        public static boolean isCaptureMove(Cell originCell, Cell targetCell) {
            int rowDiff = targetCell.getY() - originCell.getY();
            int deltaX = targetCell.getX() - originCell.getX();
            int deltaY = targetCell.getY() - originCell.getY();
            int midCol = originCell.getX() + deltaX / 2;
            int midRow = originCell.getY() + deltaY / 2;

            Cell midCell = App.getBoard()[midRow][midCol];
            CheckersPiece midPiece = midCell.getPiece();

            if (Math.abs(rowDiff) != 2){
                return false;
            }

            if (midPiece.getColour() != originCell.getPiece().getColour()) {
                return true;
            }

            return false;

        }

        public static void processMove(Cell originCell, Cell targetCell) {
            int deltaX = targetCell.getX() - originCell.getX();
            int deltaY = targetCell.getY() - originCell.getY();
            int midCol = originCell.getX() + deltaX / 2;
            int midRow = originCell.getY() + deltaY / 2;

            Cell midCell = App.getBoard()[midRow][midCol];
            CheckersPiece midPiece = midCell.getPiece();

            if (isCaptureMove(originCell, targetCell)) {
                midPiece.getPosition().setPiece(null);
            }

            targetCell.setPiece(originCell.getPiece());
            originCell.setPiece(null);

        }

        public static void promotePiece(Cell cell) {

            if (cell.getY() == 0 && App.getPlayer() == 'b') {
                cell.getPiece().setKing(true);
            }

            if (cell.getY() == 7 && App.getPlayer() == 'w') {
                cell.getPiece().setKing(true);
            }


        }
}