package gamestates;

import tetris.ButtonPress;

import java.awt.*;
import java.awt.event.KeyEvent;

public class WaitingState implements GameState {

    private final GameStateManager manager;
    private final GameState stateBeneath;
    private final int frames;
    private int counter;

    public WaitingState(GameStateManager manager, GameState stateBeneath, int frames) {
        this.stateBeneath = stateBeneath;
        this.frames = frames;
        this.manager = manager;
    }

    @Override
    public void entering() {
        counter = 0;
    }

    @Override
    public void update() {
        if (counter == frames) {
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

    }
}
