package tetris;

import java.awt.*;

public class Tetromino_I extends Tetromino {

    public static Color COLOR;

    public Tetromino_I(int x, int y) {
        super(x - Block.SIZE, y);

        blocks[0] = new Block(this.x, this.y, COLOR);
        blocks[1] = new Block(this.x - Block.SIZE, this.y, COLOR);
        blocks[2] = new Block(this.x + Block.SIZE, this.y, COLOR);
        blocks[3] = new Block(this.x + (Block.SIZE * 2), this.y, COLOR);
    }
}
