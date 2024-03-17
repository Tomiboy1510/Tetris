package main;

import gamestates.GameStateManager;
import tetris.ButtonPress;

public class GameController {

    private final boolean[] pressed; // Holds the state of each button
    private final GameStateManager manager;
    private long lastShiftTime = 0; // Last time a horizontal movement was made
    private long lastDropTime = 0; // Last time a downward movement was made
    private boolean delayEntered = false; // Becomes true AFTER a horizontal movement is first made
    private boolean delayExited = false; // Becomes true when the auto shift delay has passed

    public GameController(GameStateManager manager) {
        this.manager = manager;
        pressed = new boolean[ButtonPress.values().length];
    }

    public void press(ButtonPress p) {
        pressed[p.ordinal()] = true;
    }

    public void release(ButtonPress p) {
        pressed[p.ordinal()] = false;
        switch (p) {
            case MOVE_LEFT, MOVE_RIGHT -> {
                delayEntered = false;
                delayExited = false;
            }
        }
    }

    public void update() {
        for (int i = 0; i < pressed.length; ++i) {
            // Tetromino movements will be handled separately
            if (    i == ButtonPress.MOVE_DOWN.ordinal() ||
                    i == ButtonPress.MOVE_RIGHT.ordinal() ||
                    i == ButtonPress.MOVE_LEFT.ordinal()) {
                continue;
            }

            if (pressed[i]) {
                manager.buttonPressed(ButtonPress.values()[i]);
            }
            pressed[i] = false;
        }

        handleDrops();
        handleShifts(ButtonPress.MOVE_LEFT);
        handleShifts(ButtonPress.MOVE_RIGHT);
    }

    private void handleDrops() {
        int AUTO_DROP_INTERVAL = 50;

        if (! pressed[ButtonPress.MOVE_DOWN.ordinal()])
            return;

        if (System.currentTimeMillis() - lastDropTime > AUTO_DROP_INTERVAL) {
            manager.buttonPressed(ButtonPress.MOVE_DOWN);
            lastDropTime = System.currentTimeMillis();
        }
    }

    private void handleShifts(ButtonPress p) {
        int AUTO_SHIFT_DELAY = 150;
        int AUTO_SHIFT_INTERVAL = 60;

        if (! pressed[p.ordinal()])
            return;

        boolean delayHasPassed = System.currentTimeMillis() - lastShiftTime > AUTO_SHIFT_DELAY;

        if (    (! delayEntered)
                || (delayExited && System.currentTimeMillis() - lastShiftTime > AUTO_SHIFT_INTERVAL)
                || (! delayExited && delayHasPassed)) {
            manager.buttonPressed(p);
            lastShiftTime = System.currentTimeMillis();
        }

        delayExited = delayExited || (delayEntered && delayHasPassed);
        delayEntered = true;
    }
}
