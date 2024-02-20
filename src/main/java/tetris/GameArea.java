package tetris;

import java.awt.*;
import java.util.Random;

public class GameArea {

    private final int leftX;
    private final int rightX;
    private final int topY;
    private final int bottomY;
    private final Random random;

    private final int framesUntilDrop;
    private int dropCounter;
    private boolean gameOver;
    private Tetromino currentTetromino;
    private final Block[][] staticBlocks = new Block[10][20];

    public GameArea(int posX, int posY, int width, int height, long seed, int fps) {

        // Fronteras del área
        this.leftX = posX;
        this.rightX = posX + width;
        this.topY = posY;
        this.bottomY = posY + height;
        Block.SIZE = (width) / 10;

        // Colores de los tetrominos
        Tetromino_I.COLOR = new Color(249, 65, 68);
        Tetromino_J.COLOR = new Color(243, 114, 44);
        Tetromino_L.COLOR = new Color(248, 150, 30);
        Tetromino_O.COLOR = new Color(249, 199, 79);
        Tetromino_S.COLOR = new Color(144, 190, 109);
        Tetromino_T.COLOR = new Color(67, 170, 139);
        Tetromino_Z.COLOR = new Color(87, 117, 144);

        // Variables del juego
        framesUntilDrop = fps / 2;
        dropCounter = 0;

        random = new Random(seed);
    }

    public void update() {
        if (framesUntilDrop == dropCounter) {
            if (isAtBottom()) {
                // El tetromino aterrizó
                freezeTetromino();
                clearRows();
                spawnTetromino();
            }
            else
                // Hacer caer el tetromino
                moveTetromino(0, 1);
            dropCounter = 0;
        }
        dropCounter++;
    }

    public void reset() {
        for (int i = 0; i<10; i++)
            for (int j = 0; j < 20; j++)
                staticBlocks[i][j] = null;
        gameOver = false;
        spawnTetromino();
    }

    public void draw(Graphics2D g) {
        // Dibujar borde del área de juego
        g.setColor(Color.WHITE);
        int padding = 2;
        g.setStroke(new BasicStroke(padding));
        g.drawRect(
                leftX - padding,
                topY - padding,
                (rightX - leftX) + padding * 2,
                (bottomY - topY) + padding * 2
        );

        // Dibujar tetromino actual
        if (currentTetromino != null)
            currentTetromino.draw(g);

        // Dibujar bloques
        g.setStroke(new BasicStroke(1));
        for (Block[] row : staticBlocks)
            for (Block b : row)
                if (b != null)
                    b.draw(g);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void spawnTetromino() {
        Tetromino tetro;
        int x = (rightX + leftX) / 2;
        tetro = switch (random.nextInt(7)) {
            default -> new Tetromino_I(x, topY);
            case 1 -> new Tetromino_J(x, topY);
            case 2 -> new Tetromino_L(x, topY);
            case 3 -> new Tetromino_O(x, topY);
            case 4 -> new Tetromino_S(x, topY);
            case 5 -> new Tetromino_T(x, topY);
            case 6 -> new Tetromino_Z(x, topY);
        };
        currentTetromino = tetro;

        if (collides(currentTetromino))
            gameOver = true;
    }

    public void rotateClockWise() {
        currentTetromino.rotate(Tetromino.RotationSenses.CLOCKWISE);
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            currentTetromino.rotate(Tetromino.RotationSenses.COUNTER_CLOCKWISE);
    }

    public void rotateCounterClockWise() {
        currentTetromino.rotate(Tetromino.RotationSenses.COUNTER_CLOCKWISE);
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            currentTetromino.rotate(Tetromino.RotationSenses.CLOCKWISE);
    }

    private boolean collides(Block b) {
        for (Block[] row : staticBlocks)
            for (Block s : row) if (s != null)
                if (s.x == b.x && s.y == b.y)
                    return true;
        return false;
    }

    private boolean collides(Tetromino t) {
        for (Block b : t.getBlocks())
            if (collides(b))
                return true;
        return false;
    }

    private boolean outOfBounds(Block b) {
        return (b.x < leftX ||
                b.x + Block.SIZE > rightX ||
                b.y < topY ||
                b.y + Block.SIZE > bottomY);
    }

    private boolean outOfBounds(Tetromino t) {
        for (Block b : t.getBlocks())
            if (outOfBounds(b))
                return true;
        return false;
    }

    public void moveTetromino(int dx, int dy) {
        for (Block b : currentTetromino.getBlocks()) {
            b.x += Block.SIZE * dx;
            b.y += Block.SIZE * dy;
        }
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            moveTetromino(-1 * dx, -1 * dy);
    }

    private void freezeTetromino() {
        for (Block b : currentTetromino.getBlocks()) {
            int x = (b.x - leftX) / Block.SIZE;
            int y = (b.y - topY) / Block.SIZE;
            staticBlocks[x][y] = b;
        }
    }

    private boolean isAtBottom() {
        for (Block b : currentTetromino.getBlocks()) {
            if (b.y == bottomY - Block.SIZE)
                return true;
            for (Block[] row : staticBlocks)
                for (Block s : row) if (s != null)
                    if (s.x == b.x && s.y == b.y + Block.SIZE)
                        return true;
        }
        return false;
    }

    private void clearRows() {
        // TO-DO
    }
}
