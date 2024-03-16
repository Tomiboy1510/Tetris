package tetris;

import java.awt.*;

public abstract class Tetromino {

    int x, y;
    protected Block[] blocks = new Block[4];

    public enum RotationSenses {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    public Tetromino(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g) {
        for (Block b : blocks)
            b.draw(g);
    }

    public void draw(Graphics2D g, int x, int y) {
        for (Block b : blocks)
            b.draw(g, x + (b.x - this.x), y + (b.y - this.y));
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void rotate(RotationSenses r) {
        int pivotX = getBlocks()[0].x;
        int pivotY = getBlocks()[0].y;

        for (Block b : getBlocks()) {
            b.x -= pivotX;
            b.y -= pivotY;
            int temp = b.x;
            switch (r) {
                case CLOCKWISE:
                    b.x = (b.y) * -1;
                    b.y = temp;
                    break;
                case COUNTER_CLOCKWISE:
                    b.x = b.y;
                    b.y = temp * -1;
                default:
            }
            b.x += pivotX;
            b.y += pivotY;
        }
    }
}
