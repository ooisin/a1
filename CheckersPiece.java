package Checkers;

import java.util.HashSet;
import java.util.Set;

import static Checkers.Move.isValidMove;

public class CheckersPiece {

    private char colour;
    private Cell position;
    private boolean king;
    public CheckersPiece(char c) {
        this.colour = c;
    }

    public boolean isKing() {
        return this.king;
    }

    public void setKing(boolean isKing) { this.king = isKing; }

    public char getColour() {
        return this.colour;
    }

    public void setPosition(Cell p) {
        this.position = p;
    }

    public Cell getPosition() {
        return this.position;
    }

    public static Set<Cell> getAvailableMoves(Cell[][] board, Cell originCell) {
        //TODO: Get available moves for this piece depending on the board layout, and if the piece is a king as it can then move backwards
        Set<Cell> availableMoves = new HashSet<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isValidMove(originCell, board[i][j])) {
                    availableMoves.add(board[i][j]);
                }
            }
        }

        return availableMoves;

    }

    public void capture() {
        // capture this piece
    }

    public void promote() {
        // promote this piece
    }

    //draw the piece
    public void draw(App app) {
        app.strokeWeight(5.0f);
        if (colour == 'w') {
            app.fill(255);
            app.stroke(0);
        } else if (colour == 'b') {
            app.fill(0);
            app.stroke(255);
        }

        app.ellipse(position.getX()*App.CELLSIZE + App.CELLSIZE/2, position.getY()*App.CELLSIZE + App.CELLSIZE/2, App.CELLSIZE*0.8f, App.CELLSIZE*0.8f);
        app.noStroke();

        if (isKing()) {
            app.fill(255,0,0);
            app.ellipse(position.getX() * App.CELLSIZE + App.CELLSIZE / 2, position.getY() * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.3f, App.CELLSIZE * 0.3f);
        }



    }
}
