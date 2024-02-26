package gamestates;

import tetris.ButtonPress;
import tetris.GameArea;
import tetris.GameSettings;

import java.awt.*;

public class PlayingState implements GameState {

    private final GameStateManager manager;
    private final GameArea gameArea;
    private final int fps;

    public PlayingState(GameStateManager manager, GameSettings settings) {
        this.manager = manager;
        this.fps = settings.fps();

        // Initialize game area ensuring that its height in pixels is a multiple of 20
        int pretendedHeight = (int) (settings.height() * 0.9);
        int remainder = pretendedHeight % 20;
        int gameAreaHeight = remainder == 0 ?
                pretendedHeight :
                pretendedHeight - remainder;
        int gameAreaWidth = gameAreaHeight / 2;

        gameArea = new GameArea(
                settings.width() / 2 - gameAreaWidth / 2,
                settings.height() / 2 - gameAreaHeight / 2,
                gameAreaWidth,
                gameAreaHeight,
                settings.seed(),
                settings.fps(),
                settings.palette()
        );
    }

    @Override
    public void entering() {
        if (gameArea.isGameOver()) {
            gameArea.reset();
        }
    }

    @Override
    public void update() {
        if (gameArea.isGameOver()) {
            manager.push(new WaitingState(manager, this, fps * 3));
        } else {
            gameArea.update();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        gameArea.draw(g);
        // Draw interface elements (Score, next tetromino and such)
        //gameArea.getNextTetromino().draw(g);
    }

    @Override
    public void exiting() {

    }

    @Override
    public void buttonPressed(ButtonPress p) {
        switch (p) {
            case MOVE_LEFT -> gameArea.moveLeft();
            case MOVE_RIGHT -> gameArea.moveRight();
            case MOVE_DOWN -> gameArea.moveDown();
            case ROTATE_CLOCKWISE -> gameArea.rotateClockWise();
            case ROTATE_COUNTER_CLOCKWISE -> gameArea.rotateCounterClockWise();
            case PAUSE -> manager.push(new PauseState(manager, this));
            case RESET -> {
                gameArea.reset();
                manager.push(new WaitingState(manager, this, fps));
            }
        }
    }
}
