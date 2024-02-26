package tetris;

import java.awt.*;
import java.util.Random;

public class GameArea {

    private final int leftX, rightX, topY, bottomY;
    private final Random random;

    private final int framesUntilDrop;
    private int dropCounter;
    private boolean gameOver;
    private int score;

    private Tetromino currentTetromino, nextTetromino;
    private final Block[][] staticBlocks = new Block[10][20];

    public GameArea(int posX, int posY, int width, int height, long seed, int fps, Color[] palette) {

        // Area bounds
        this.leftX = posX;
        this.rightX = posX + width;
        this.topY = posY;
        this.bottomY = posY + height;
        Block.SIZE = (width) / 10;

        // Tetromino colors
        Tetromino_I.COLOR = palette[0];
        Tetromino_J.COLOR = palette[1];
        Tetromino_L.COLOR = palette[2];
        Tetromino_O.COLOR = palette[3];
        Tetromino_S.COLOR = palette[4];
        Tetromino_T.COLOR = palette[5];
        Tetromino_Z.COLOR = palette[6];

        // Game variables
        score = 0;
        framesUntilDrop = fps / 2;
        dropCounter = 0;
        gameOver = true;
        random = new Random(seed);
        nextTetromino = getTetromino((rightX + leftX) / 2, topY);
    }

    public void update() {
        if (framesUntilDrop == dropCounter) {
            if (isAtBottom()) {
                // Tetromino landed
                freezeTetromino();
                clearRows();
                spawnTetromino();
            } else
                moveDown();
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
        // Draw game area border
        g.setColor(Color.WHITE);
        int padding = 2;
        g.setStroke(new BasicStroke(padding));
        g.drawRect(
                leftX - padding,
                topY - padding,
                (rightX - leftX) + padding * 2,
                (bottomY - topY) + padding * 2
        );

        // Draw current tetromino
        if (currentTetromino != null)
            currentTetromino.draw(g);

        // Draw blocks
        g.setStroke(new BasicStroke(1));
        for (Block[] row : staticBlocks)
            for (Block b : row)
                if (b != null)
                    b.draw(g);
    }

    public boolean isGameOver() {
        return gameOver;
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

    public void moveLeft() {
        for (Block b : currentTetromino.getBlocks())
            b.x -= Block.SIZE;
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            moveRight();
    }

    public void moveRight() {
        for (Block b : currentTetromino.getBlocks())
            b.x += Block.SIZE;
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            moveLeft();
    }

    public void moveDown() {
        for (Block b : currentTetromino.getBlocks())
            b.y += Block.SIZE;
        if (collides(currentTetromino) || outOfBounds(currentTetromino))
            for (Block b : currentTetromino.getBlocks())
                b.y -= Block.SIZE;
        else
            dropCounter = 0;
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
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

    private void freezeTetromino() {
        for (Block b : currentTetromino.getBlocks()) {
            int x = (b.x - leftX) / Block.SIZE;
            int y = (b.y - topY) / Block.SIZE;
            staticBlocks[x][y] = b;
        }
    }

    private void spawnTetromino() {
        currentTetromino = nextTetromino;
        nextTetromino = getTetromino((rightX + leftX) / 2, topY);
        if (collides(currentTetromino))
            gameOver = true;
    }

    private Tetromino getTetromino(int x, int y) {
        return switch (random.nextInt(7)) {
            case 0 -> new Tetromino_I(x, y);
            case 1 -> new Tetromino_J(x, y);
            case 2 -> new Tetromino_L(x, y);
            case 3 -> new Tetromino_O(x, y);
            case 4 -> new Tetromino_S(x, y);
            case 5 -> new Tetromino_T(x, y);
            case 6 -> new Tetromino_Z(x, y);
            default -> throw new IllegalStateException("Unexpected value: " + random.nextInt(7));
        };
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
        int filledRows = 0;
        for (int row = 0; row < 20; row++)
            if (isRowFilled(row)) {
                filledRows++;
                collapse(row);
            }
        score += switch (filledRows) {
            default -> 0;
            case 1 -> 40;
            case 2 -> 100;
            case 3 -> 300;
            case 4 -> 1200;
        };
        //System.out.println("Score: " + score);
    }

    private boolean isRowFilled(int row) {
        for (int i = 0; i < 10; i++)
            if (staticBlocks[i][row] == null)
                return false;
        return true;
    }

    private void collapse(int row) {
        // Empty row
        for (int i = 0; i < 10; i++)
            staticBlocks[i][row] = null;

        if (row == 0)
            return;

        // Shift all rows above
        for (int r = row; r > 0; r--)
            for (int col = 0; col < 10; col++) {
                staticBlocks[col][r] = staticBlocks[col][r - 1];
                // Shift block coordinates vertically
                if (staticBlocks[col][r] != null)
                    staticBlocks[col][r].y += Block.SIZE;
            }
        // Empty first row
        for (int col = 0; col < 10; col++)
            staticBlocks[col][0] = null;
    }
}
