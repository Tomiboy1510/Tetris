package tetris;

import java.awt.*;

public class Tetromino_L extends Tetromino {

    public static Color COLOR;

    public Tetromino_L(int x, int y) {
        super(x - Block.SIZE, y + Block.SIZE);

        blocks[0] = new Block(this.x, this.y, COLOR);
        blocks[1] = new Block(this.x, this.y - Block.SIZE, COLOR);
        blocks[2] = new Block(this.x, this.y + Block.SIZE, COLOR);
        blocks[3] = new Block(this.x + Block.SIZE, this.y + Block.SIZE, COLOR);
    }
}
