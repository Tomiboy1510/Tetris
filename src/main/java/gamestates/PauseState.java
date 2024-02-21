package gamestates;

import tetris.ButtonPress;

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
    public void buttonPressed(ButtonPress p) {
        // Salir de la pausa
        switch (p) {
            case PAUSE -> manager.pop();
        }
    }
}
