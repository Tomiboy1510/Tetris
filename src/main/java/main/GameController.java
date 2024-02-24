package main;

import gamestates.GameStateManager;
import tetris.ButtonPress;

public class GameController {

    private boolean[] pressed;
    private GameStateManager manager;

    public GameController(GameStateManager manager) {
        this.manager = manager;
        pressed = new boolean[ButtonPress.values().length];
    }

    public void press(ButtonPress p) {
        pressed[p.ordinal()] = true;
    }

    public void release(ButtonPress p) {
        pressed[p.ordinal()] = false;
    }

    public void update() {
        for (int i = 0; i < pressed.length; ++i) {
            if (pressed[i]) {
                manager.buttonPressed(ButtonPress.values()[i]);
            }
            pressed[i] = false;
        }
    }
}
