package tetris;

import java.awt.*;

public class Tetromino_O extends Tetromino {

    public static Color COLOR;

    public Tetromino_O(int x, int y) {
        x -= Block.SIZE; // Offset
        blocks[0] = new Block(x, y, COLOR);
        blocks[1] = new Block(x + Block.SIZE, y, COLOR);
        blocks[2] = new Block(x, y + Block.SIZE, COLOR);
        blocks[3] = new Block(x + Block.SIZE, y + Block.SIZE, COLOR);
    }

    public void rotateClockWise() {
        // No hacer nada
    }

    public void rotateCounterClockWise() {
        // No hacer nada
    }
}
