package Checkers;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.sql.SQLOutput;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

import static Checkers.CheckersPiece.getAvailableMoves;
import static Checkers.Move.processMove;
import static Checkers.Move.promotePiece;

public class App extends PApplet {
    public List<Cell> availableMoves = new ArrayList<>();
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][] {
        //default - white & black
        {
                {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
        },
        //green
        {
                {105, 138, 76}, //when on white cell
                {105, 138, 76} //when on black cell
        },
        //blue
        {
                {196,224,232},
                {170,210,221}
        }
	};

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;

    // Data Storage
    private static Cell[][] board;
    private CheckersPiece currentSelected;
    private HashSet<Cell> selectedCell;
    private static HashMap<Character, HashSet<CheckersPiece>> piecesInPlay = new HashMap<>();
    private static char currentPlayer = 'b';

    public static HashMap<Character, HashSet<CheckersPiece>> getRemainingPieces() {
        return piecesInPlay;
    }

    public static char getPlayer() {
        return currentPlayer;
    }
    public static Cell[][] getBoard() {return board;}


    public App() {
        
    }

    /**
     * Initialise the setting of the window size.
    */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

	@Override
    public void setup() {
        frameRate(FPS);


        this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
        HashSet<CheckersPiece> w = new HashSet<>();
        HashSet<CheckersPiece> b = new HashSet<>();
        piecesInPlay.put('w', w);
        piecesInPlay.put('b', b);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(j,i);

                if ((j+i) % 2 == 1){
                    if (i < 3) {
                        // white
                        board[i][j].setPiece(new CheckersPiece('w'));
                        w.add(board[i][j].getPiece());
                    } else if (i >= 5) {
                        board[i][j].setPiece(new CheckersPiece('b'));
                        b.add(board[i][j].getPiece());
                    }
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
	@Override
    public void keyPressed(){

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
	@Override
    public void keyReleased(){

    }


    @Override
    public void mousePressed(MouseEvent e) {
        // availableMoves.clear();
        //Check if the user clicked on a piece which is theirs - make sure only who current turn it is, can click on pieces
		int x = e.getX();
        int y = e.getY();
        if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT) return;

        Cell clicked = board[y/App.CELLSIZE][x/App.CELLSIZE];
        if (clicked.getPiece() != null && clicked.getPiece().getColour() == currentPlayer) {

            if (clicked.getPiece()  == currentSelected) {
                currentSelected = null;
                availableMoves.clear();
            } else {
                currentSelected = clicked.getPiece();
                availableMoves.clear();
            } if (currentSelected != null) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if (Move.isValidMove(currentSelected.getPosition(), board[i][j])) {
                            availableMoves.add(board[i][j]);
                        }
                    }
                }
            }
        } else {
            if (currentSelected != null && availableMoves.contains(clicked)) {
                processMove(currentSelected.getPosition(), clicked);
                promotePiece(clicked);
                availableMoves.clear();
                currentPlayer = (currentPlayer == 'b') ? 'w' : 'b';
            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
	@Override
    public void draw() {
        this.noStroke();
        //white background
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]);
		//draw the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                    if (currentSelected != null && board[i][j].getPiece() == currentSelected) {
                        this.setFill(1, (i + j) % 2);
                        this.rect(j * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    } else if ((i + j) % 2 == 1) {
                        // black cell
                        this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
                        this.rect(j * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }
                    if (availableMoves.contains(board[i][j])) {
                    this.setFill(2, 0);
                    rect(j * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }

                    board[i][j].draw(this);

            }

            // check if game over
            if (piecesInPlay.get('w').isEmpty() || piecesInPlay.get('b').isEmpty()) {
                fill(255);
                // stroke(0);
                // strokeWeight(5.0f);
                rect(App.WIDTH * 0.19f, App.HEIGHT * 0.33f, App.CELLSIZE * 3.1f, App.CELLSIZE * 0.92f);
                fill(200, 0, 200);
                textSize(24.0f);
                if (piecesInPlay.get('w').isEmpty()) {
                    text("Black Wins!", App.WIDTH * 0.2f, App.HEIGHT * 0.4f);
                } else if (piecesInPlay.get('b').isEmpty()) {
                    text("White Wins!", App.WIDTH * 0.2f, App.HEIGHT * 0.4f);
                }
            }
        }
    }


	/**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
	public void setFill(int colourCode, int blackOrWhite) {
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }


}
