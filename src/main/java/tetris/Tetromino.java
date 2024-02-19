package tetris;

import java.awt.*;

public abstract class Tetromino {

    protected Block[] blocks = new Block[4];

    public void draw(Graphics2D g) {
        for (Block b : blocks)
            b.draw(g);
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void rotateClockWise() {
        int pivotX = getBlocks()[0].x;
        int pivotY = getBlocks()[0].y;

        for (Block b : getBlocks()) {
            b.x -= pivotX;
            b.y -= pivotY;

            int temp = b.x;
            b.x = (b.y) * -1;
            b.y = temp;

            b.x += pivotX;
            b.y += pivotY;
        }
    }

    public void rotateCounterClockWise() {
        int pivotX = getBlocks()[0].x;
        int pivotY = getBlocks()[0].y;

        for (Block b : getBlocks()) {
            b.x -= pivotX;
            b.y -= pivotY;

            int temp = b.x;
            b.x = b.y;
            b.y = temp * -1;

            b.x += pivotX;
            b.y += pivotY;
        }
    }
}
