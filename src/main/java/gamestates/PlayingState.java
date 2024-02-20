package gamestates;

import tetris.GameArea;
import tetris.GameSettings;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayingState implements GameState {

    private final GameStateManager manager;
    private final GameArea gameArea;
    private final int fps;

    public PlayingState(GameStateManager manager, GameSettings settings) {
        this.manager = manager;
        this.fps = settings.fps();

        // Inicializar área de juego (asegurando que la altura sea múltiplo de 20)
        // La altura es el doble de 10, que es el ancho en bloques del área de juego
        int pretendedHeight = (int)(settings.HEIGHT() * 0.9);
        int remainder = pretendedHeight % 20;
        int gameAreaHeight = remainder == 0 ?
                pretendedHeight :
                pretendedHeight - remainder;
        int gameAreaWidth = gameAreaHeight/2;

        gameArea = new GameArea(
                settings.WIDTH()/2 - gameAreaWidth/2,
                settings.HEIGHT()/2 - gameAreaHeight/2,
                gameAreaWidth,
                gameAreaHeight,
                settings.seed(),
                settings.fps()
        );
    }

    @Override
    public void entering() {
        gameArea.reset();
    }

    @Override
    public void update() {
        if (gameArea.isGameOver()) {
            manager.push(new WaitingState(manager, this, fps * 2));
            return;
        }
        gameArea.update();
    }

    @Override
    public void draw(Graphics2D g) {
        gameArea.draw(g);
        // Dibujar elementos de la interfaz
    }

    @Override
    public void exiting() {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> gameArea.moveTetromino(-1, 0);
            case KeyEvent.VK_D -> gameArea.moveTetromino(1, 0);
            case KeyEvent.VK_S -> gameArea.moveTetromino(0, 1);
            case KeyEvent.VK_SPACE -> gameArea.rotateClockWise();
            case KeyEvent.VK_Z -> gameArea.rotateCounterClockWise();
            case KeyEvent.VK_P -> manager.push(new PauseState(manager, this));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
