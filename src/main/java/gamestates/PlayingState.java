package gamestates;

import tetris.*;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PlayingState implements GameState {

    private final GameStateManager manager;
    private final int leftX, rightX, topY, bottomY;
    private final Random random;
    private final int fps;

    private int framesUntilDrop;
    private int dropCounter;
    private boolean gameOver;
    private int score;
    private int topScore;
    private int level;
    private int clearedRows;
    private Tetromino currentTetromino, nextTetromino;
    private final Block[][] staticBlocks = new Block[10][20];

    public PlayingState(GameStateManager manager, GameSettings settings) {
        this.manager = manager;
        fps = settings.fps();

        // Initialize game area ensuring that its height in pixels is a multiple of 20
        int pretendedHeight = (int) (settings.height() * 0.9);
        int remainder = pretendedHeight % 20;
        int gameAreaHeight = remainder == 0 ?
                pretendedHeight :
                pretendedHeight - remainder;
        int gameAreaWidth = gameAreaHeight / 2;

        leftX = settings.width() / 2 - gameAreaWidth / 2;
        rightX = leftX + gameAreaWidth;
        topY = settings.height() / 2 - gameAreaHeight / 2;
        bottomY = topY + gameAreaHeight;

        Block.SIZE = gameAreaWidth / 10;

        Tetromino_I.COLOR = settings.palette()[0];
        Tetromino_J.COLOR = settings.palette()[1];
        Tetromino_L.COLOR = settings.palette()[2];
        Tetromino_O.COLOR = settings.palette()[3];
        Tetromino_S.COLOR = settings.palette()[4];
        Tetromino_T.COLOR = settings.palette()[5];
        Tetromino_Z.COLOR = settings.palette()[6];

        // Game variables
        loadTopSCore();
        gameOver = true;
        random = new Random(settings.seed());
        nextTetromino = getTetromino((rightX + leftX) / 2, topY);
    }

    @Override
    public void entering() {
        if (gameOver) {
            reset();
        }
    }

    @Override
    public void update() {
        if (framesUntilDrop == dropCounter) {
            if (isAtBottom()) {
                // Tetromino landed
                freezeTetromino();
                clearRows();
                spawnTetromino();
                if (gameOver) {
                    manager.push(new WaitingState(manager, fps * 3));
                    if (score > topScore) {
                        topScore = score;
                        saveTopScore();
                    }
                } else {
                    manager.push(new WaitingState(manager, fps / 8));
                }
            } else {
                moveDown();
                score--; // Score should only go up by 1 if user pressed 'down'
                // Push waiting state?
            }
            dropCounter = 0;
        }
        dropCounter++;
    }

    @Override
    public void draw(Graphics2D g) {
        // Set stroke and color
        g.setColor(Color.WHITE);
        int padding = 2;
        g.setStroke(new BasicStroke(padding));

        // Draw game area border
        g.drawRect(
                leftX - padding,
                topY - padding,
                (rightX - leftX) + padding * 2,
                (bottomY - topY) + padding * 2
        );

        // Draw next tetromino frame
        int nextTetroFrameX = rightX + (rightX - leftX) / 20;
        int nextTetroFrameSize = (rightX - leftX) / 2;
        g.drawRect(
                nextTetroFrameX,
                topY,
                nextTetroFrameSize,
                nextTetroFrameSize
        );

        // Draw interface elements (Score, next tetromino and such)
        drawUiText(g);

        // Draw next tetromino
        nextTetromino.draw(
                g,
                nextTetroFrameX + (int)(nextTetroFrameSize / 3.5),
                topY + (int)(nextTetroFrameSize / 2.5)
        );

        // Draw current tetromino
        if (currentTetromino != null) {
            currentTetromino.draw(g);
        }

        // Draw blocks
        g.setStroke(new BasicStroke(1));
        for (Block[] row : staticBlocks) {
            for (Block b : row) {
                if (b != null) {
                    b.draw(g);
                }
            }
        }
    }

    @Override
    public void exiting() {

    }

    @Override
    public void buttonPressed(ButtonPress p) {
        switch (p) {
            case MOVE_LEFT -> moveLeft();
            case MOVE_RIGHT -> moveRight();
            case MOVE_DOWN -> moveDown();
            case ROTATE_CLOCKWISE -> rotateClockWise();
            case ROTATE_COUNTER_CLOCKWISE -> rotateCounterClockWise();
            case PAUSE -> manager.push(new PauseState(manager));
            case RESET -> {
                reset();
                manager.push(new WaitingState(manager, fps));
            }
        }
    }

    private void drawUiText(Graphics2D g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (rightX - leftX) / 12));
        int uiTextY = topY + (rightX - leftX) / 2 + (rightX - leftX) / 8;

        g.drawString(
                "Level: " + level,
                rightX + (rightX - leftX) / 20,
                uiTextY
        );

        uiTextY += (rightX - leftX) / 8;
        g.drawString(
                "Score: " + score,
                rightX + (rightX - leftX) / 20,
                uiTextY
        );

        uiTextY += (rightX - leftX) / 8;
        g.drawString(
                "Top score: " + topScore,
                rightX + (rightX - leftX) / 20,
                uiTextY
        );

        uiTextY += (rightX - leftX) / 8;
        g.drawString(
                "Lines cleared: " + clearedRows,
                rightX + (rightX - leftX) / 20,
                uiTextY
        );
    }

    private void reset() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                staticBlocks[i][j] = null;
            }
        }
        score = 0;
        clearedRows = 0;
        setLevel(0);
        dropCounter = 0;
        gameOver = false;
        spawnTetromino();
    }

    private void rotateClockWise() {
        currentTetromino.rotate(Tetromino.RotationSenses.CLOCKWISE);
        if (collides(currentTetromino) || outOfBounds(currentTetromino)) {
            currentTetromino.rotate(Tetromino.RotationSenses.COUNTER_CLOCKWISE);
        }
    }

    private void rotateCounterClockWise() {
        currentTetromino.rotate(Tetromino.RotationSenses.COUNTER_CLOCKWISE);
        if (collides(currentTetromino) || outOfBounds(currentTetromino)) {
            currentTetromino.rotate(Tetromino.RotationSenses.CLOCKWISE);
        }
    }

    private void moveLeft() {
        for (Block b : currentTetromino.getBlocks()) {
            b.x -= Block.SIZE;
        }
        if (collides(currentTetromino) || outOfBounds(currentTetromino)) {
            moveRight();
        }
    }

    private void moveRight() {
        for (Block b : currentTetromino.getBlocks()) {
            b.x += Block.SIZE;
        }
        if (collides(currentTetromino) || outOfBounds(currentTetromino)) {
            moveLeft();
        }
    }

    private void moveDown() {
        for (Block b : currentTetromino.getBlocks()) {
            b.y += Block.SIZE;
        }
        if (collides(currentTetromino) || outOfBounds(currentTetromino)) {
            for (Block b : currentTetromino.getBlocks()) {
                b.y -= Block.SIZE;
            }
        } else {
            dropCounter = 0;
            score++;
        }
    }

    private boolean collides(Block b) {
        for (Block[] row : staticBlocks) {
            for (Block s : row)
                if (s != null) {
                    if (s.x == b.x && s.y == b.y) {
                        return true;
                    }
                }
        }
        return false;
    }

    private boolean collides(Tetromino t) {
        for (Block b : t.getBlocks()) {
            if (collides(b)) {
                return true;
            }
        }
        return false;
    }

    private boolean outOfBounds(Block b) {
        return (b.x < leftX ||
                b.x + Block.SIZE > rightX ||
                b.y < topY ||
                b.y + Block.SIZE > bottomY);
    }

    private boolean outOfBounds(Tetromino t) {
        for (Block b : t.getBlocks()) {
            if (outOfBounds(b)) {
                return true;
            }
        }
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
        if (collides(currentTetromino)) {
            gameOver = true;
        }
    }

    private Tetromino getTetromino(int x, int y) {
        return switch (random.nextInt(7)) {
            default -> new Tetromino_I(x, y);
            case 1 -> new Tetromino_J(x, y);
            case 2 -> new Tetromino_L(x, y);
            case 3 -> new Tetromino_O(x, y);
            case 4 -> new Tetromino_S(x, y);
            case 5 -> new Tetromino_T(x, y);
            case 6 -> new Tetromino_Z(x, y);
        };
    }

    private boolean isAtBottom() {
        for (Block b : currentTetromino.getBlocks()) {
            // At bottom of game area?
            if (b.y == bottomY - Block.SIZE) {
                return true;
            }
            // On top of other blocks?
            int x = (b.x - leftX) / Block.SIZE;
            int y = (b.y - topY) / Block.SIZE;
            if (staticBlocks[x][y + 1] != null) {
                return true;
            }
        }
        return false;
    }

    private void clearRows() {
        // Clear rows
        int filledRows = 0;
        for (int row = 0; row < 20; row++) {
            if (isRowFilled(row)) {
                filledRows++;
                collapse(row);
            }
        }

        // Increase level and speed up game
        clearedRows += filledRows;
        setLevel((clearedRows - clearedRows % 10) / 10);

        // Increase score
        score += (level + 1) * switch (filledRows) {
            default -> 0;
            case 1 -> 40;
            case 2 -> 100;
            case 3 -> 300;
            case 4 -> 1200;
        };
    }

    private boolean isRowFilled(int row) {
        for (int i = 0; i < 10; i++) {
            if (staticBlocks[i][row] == null) {
                return false;
            }
        }
        return true;
    }

    private void collapse(int row) {
        // Empty row
        for (int i = 0; i < 10; i++) {
            staticBlocks[i][row] = null;
        }

        if (row == 0) {
            return;
        }

        // Shift all rows above
        for (int r = row; r > 0; r--) {
            for (int col = 0; col < 10; col++) {
                staticBlocks[col][r] = staticBlocks[col][r - 1];
                // Shift block coordinates vertically
                if (staticBlocks[col][r] != null) {
                    staticBlocks[col][r].y += Block.SIZE;
                }
            }
        }

        // Empty first row
        for (int col = 0; col < 10; col++) {
            staticBlocks[col][0] = null;
        }
    }

    private void setLevel(int l) {
        level = l;
        framesUntilDrop = fps / (level + 2);
    }

    private void loadTopSCore() {
        try (Scanner scanner = new Scanner(new File("topscore.txt"))) {
            topScore = scanner.nextInt();
        } catch (IOException | java.util.NoSuchElementException e) {
            this.topScore = 0;
        }
    }

    private void saveTopScore() {
        try (FileWriter writer = new FileWriter("topscore.txt")) {
            writer.write(Integer.toString(topScore));
        } catch (IOException e) {
            // Do nothing
        }
    }
}
