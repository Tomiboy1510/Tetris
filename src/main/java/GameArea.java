import tetris.Block;
import tetris.Tetromino;
import tetris.Tetromino_O;

import java.awt.*;

public class GameArea {

    private final int leftX;
    private final int rightX;
    private final int topY;
    private final int bottomY;

    private Tetromino currentTetromino;
    private final Block[][] staticBlocks = new Block[20][10];

    public GameArea(int posX, int posY, int width, int height) {

        this.leftX = posX;
        this.rightX = posX + width;
        this.topY = posY;
        this.bottomY = posY + height;

        Block.size = (rightX - leftX) / 10;
        currentTetromino = getTetromino();
    }

    public void update() {

    }

    public void draw(Graphics2D g) {

        // Dibujar borde del área de juego
        g.setColor(Color.WHITE);
        int padding = 2;
        g.setStroke(new BasicStroke(padding));
        g.drawRect(
                leftX - padding,
                topY - padding,
                (rightX - leftX) + padding*2,
                (bottomY - topY) + padding*2
        );

        // Dibujar tetromino actual
        currentTetromino.draw(g);

        // Dibujar bloques
        g.setStroke(new BasicStroke(1));
        for (Block[] row : staticBlocks) {
            for (Block b : row) {
                if (b != null)
                    b.draw(g);
            }
        }
    }

    private Tetromino getTetromino() {
        return new Tetromino_O(leftX, topY);
    }
}
