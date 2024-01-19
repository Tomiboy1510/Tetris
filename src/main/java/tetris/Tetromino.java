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

    public abstract Tetromino rotate(); //Retorna un nuevo tetromino rotado 90 grados en sentido horario
}
