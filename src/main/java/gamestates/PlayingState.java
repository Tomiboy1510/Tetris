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

        // Inicializar área de juego (asegurando que la altura sea múltiplo de 20)
        // La altura es el doble de 10, que es el ancho en bloques del área de juego
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
        if (gameArea.isGameOver())
            gameArea.reset();
    }

    @Override
    public void update() {
        if (gameArea.isGameOver()) {
            manager.push(new WaitingState(manager, this, fps));
        } else
            gameArea.update();
    }

    @Override
    public void draw(Graphics2D g) {
        gameArea.draw(g);
        // Dibujar elementos de la interfaz
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
