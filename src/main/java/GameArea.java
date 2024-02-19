import tetris.*;

import java.awt.*;
import java.util.Random;

public class GameArea {

    private final int leftX;
    private final int rightX;
    private final int topY;
    private final int bottomY;

    private boolean gameOver;
    private final int framesUntilDrop;
    private int dropCounter;
    private final Random random;
    private boolean pause;

    private Tetromino currentTetromino;
    private final Block[][] staticBlocks = new Block[10][20];

    public GameArea(int posX, int posY, int width, int height, long seed, int fps) {

        // Fronteras del área
        this.leftX = posX;
        this.rightX = posX + width;
        this.topY = posY;
        this.bottomY = posY + height;

        // Colores de los tetrominos
        Tetromino_I.COLOR = new Color(249, 65, 68);
        Tetromino_J.COLOR = new Color(243, 114, 44);
        Tetromino_L.COLOR = new Color(248, 150, 30);
        Tetromino_O.COLOR = new Color(249, 199, 79);
        Tetromino_S.COLOR = new Color(144, 190, 109);
        Tetromino_T.COLOR = new Color(67, 170, 139);
        Tetromino_Z.COLOR = new Color(87, 117, 144);

        // Variables del juego
        gameOver = false;
        random = new Random(seed);
        Block.SIZE = (rightX - leftX) / 10;
        framesUntilDrop = fps / 2;
        dropCounter = 0;
        pause = false;

        spawnTetromino();
    }

    public void update() {

        if (pause)
            return;

        // Chequear Gameover
        if (gameOver) {

        }

        // Asentar el tetromino si se cumplió el intervalo
        if (framesUntilDrop == dropCounter) {
            // Spawnear otro tetromino si el actual llegó al fondo
            if (isAtBottom())
                changeTetromino();
            else
                moveDown();
            dropCounter = 0;
        }
        dropCounter++;

        // Chequear filas llenas
        

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
        for (Block[] row : staticBlocks)
            for (Block b : row)
                if (b != null)
                    b.draw(g);
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

        if (collides())
            gameOver = true;
    }

    public void rotateClockWise() {
        currentTetromino.rotateClockWise();
        if (collides() || outOfBounds())
            currentTetromino.rotateCounterClockWise();
    }

    public void rotateCounterClockWise() {
        currentTetromino.rotateCounterClockWise();
        if (collides() || outOfBounds())
            currentTetromino.rotateClockWise();
    }

    private boolean collides() {
        for (Block[] row : staticBlocks)
            for (Block s : row) if (s != null)
                for (Block b : currentTetromino.getBlocks())
                    if (s.x == b.x && s.y == b.y)
                        return true;
        return false;
    }

    private boolean outOfBounds() {
        for (Block b : currentTetromino.getBlocks())
            if (b.x < leftX || b.x + Block.SIZE > rightX || b.y < topY || b.y + Block.SIZE > bottomY)
                return true;
        return false;
    }

    public void moveLeft() {
        for (Block b : currentTetromino.getBlocks())
            b.x -= Block.SIZE;
        if (collides() || outOfBounds())
            moveRight();
    }

    public void moveRight() {
        for (Block b : currentTetromino.getBlocks())
            b.x += Block.SIZE;
        if (collides() || outOfBounds())
            moveLeft();
    }

    public void moveDown() {
        for (Block b : currentTetromino.getBlocks())
            b.y += Block.SIZE;
        if (collides() || outOfBounds())
            for (Block b : currentTetromino.getBlocks())
                b.y -= Block.SIZE;
    }

    public void pause() {
        pause = !pause;
    }

    private void changeTetromino() {
        for (Block b : currentTetromino.getBlocks()) {
            int x = (b.x - leftX) / Block.SIZE;
            int y = (b.y - topY) / Block.SIZE;
            staticBlocks[x][y] = b;
        }
        spawnTetromino();
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
}
