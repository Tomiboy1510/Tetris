package gamestates;

import tetris.ButtonPress;

import java.awt.*;

public class WaitingState implements GameState {

    private final GameStateManager manager;
    private final GameState stateBeneath;
    private final int FRAMES;
    private int counter;

    public WaitingState(GameStateManager manager, int frames) {
        this.stateBeneath = manager.getCurrentState();
        this.FRAMES = frames;
        this.manager = manager;
    }

    @Override
    public void entering() {
        counter = 0;
    }

    @Override
    public void update() {
        if (counter == FRAMES) {
            manager.pop();
            return;
        }
        counter++;
    }

    @Override
    public void draw(Graphics2D g) {
        stateBeneath.draw(g);
    }

    @Override
    public void exiting() {

    }

    @Override
    public void buttonPressed(ButtonPress p) {
        switch (p) {
            case PAUSE -> manager.push(new PauseState(manager));
            case RESET -> {
                manager.pop();
                stateBeneath.buttonPressed(ButtonPress.RESET);
            }
        }
    }
}
