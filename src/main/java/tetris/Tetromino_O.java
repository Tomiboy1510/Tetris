package tetris;

import java.awt.*;

public class Tetromino_O extends Tetromino {

    private static final Color color = Color.ORANGE;

    public Tetromino_O(int x, int y) {
        blocks[0] = new Block(x, y, color);
        blocks[1] = new Block(x + Block.size, y, color);
        blocks[2] = new Block(x, y + Block.size, color);
        blocks[3] = new Block(x + Block.size, y + Block.size, color);
    }

    @Override
    public Tetromino rotate() {
        return this;
    }
}
