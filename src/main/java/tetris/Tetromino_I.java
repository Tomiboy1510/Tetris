package tetris;

import java.awt.*;

public class Tetromino_I extends Tetromino {

    public static Color COLOR;

    public Tetromino_I(int x, int y) {
        x -= Block.SIZE; // Offset
        blocks[0] = new Block(x, y, COLOR);
        blocks[1] = new Block(x - Block.SIZE, y, COLOR);
        blocks[2] = new Block(x + Block.SIZE, y, COLOR);
        blocks[3] = new Block(x + (Block.SIZE * 2), y, COLOR);
    }
}
