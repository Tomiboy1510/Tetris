package gamestates;

import tetris.ButtonPress;

import java.awt.*;

public class PauseState implements GameState {

    private final GameStateManager manager;
    private final GameState stateBeneath;

    public PauseState(GameStateManager manager, GameState stateBeneath) {
        this.manager = manager;
        this.stateBeneath = stateBeneath;
    }



    @Override
    public void entering() {
        // Play sound?
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        // Render game area
        stateBeneath.draw(g);

        // Show "PAUSE" sign?

    }

    @Override
    public void exiting() {
        // Play sound?
    }

    @Override
    public void buttonPressed(ButtonPress p) {
        switch (p) {
            case PAUSE -> manager.pop();
            case RESET -> {
                manager.pop();
                stateBeneath.buttonPressed(ButtonPress.RESET);
            }
        }
    }
}
