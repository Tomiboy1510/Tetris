package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseState implements GameState {

    private final GameStateManager manager;
    private final GameState stateBeneath;

    public PauseState(GameStateManager manager, GameState stateBeneath) {
        this.manager = manager;
        this.stateBeneath = stateBeneath;
    }



    @Override
    public void entering() {
        // Reproducir sonido?
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        // Mostrar juego
        stateBeneath.draw(g);

        // Mostrar cartel de PAUSA

    }

    @Override
    public void exiting() {
        // Reproducir sonido?
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Salir de la pausa!
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P -> manager.pop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
