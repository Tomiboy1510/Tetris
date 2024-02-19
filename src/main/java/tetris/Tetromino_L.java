package tetris;

import java.awt.*;

public class Tetromino_L extends Tetromino {

    public static Color COLOR;

    public Tetromino_L(int x, int y) {
        x -= Block.SIZE;
        y += Block.SIZE; // Offset
        blocks[0] = new Block(x, y, COLOR);
        blocks[1] = new Block(x, y - Block.SIZE, COLOR);
        blocks[2] = new Block(x, y + Block.SIZE, COLOR);
        blocks[3] = new Block(x + Block.SIZE, y + Block.SIZE, COLOR);
    }
}
