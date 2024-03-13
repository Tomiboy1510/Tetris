package gamestates;

import tetris.ButtonPress;
import tetris.GameSettings;

import java.awt.*;
import java.util.Stack;

public class GameStateManager {

    private final Stack<GameState> states;
    private final GameSettings settings;

    public GameStateManager(GameSettings settings) {
        this.settings = settings;
        states = new Stack<>();
        push(new PlayingState(this));
    }

    public void push(GameState state) {
        states.push(state);
        state.entering();
    }

    public void pop() {
        states.pop().exiting();
        states.peek().entering();
    }

    public void update() {
        states.peek().update();
    }

    public void draw(Graphics2D g) {
        states.peek().draw(g);
    }

    public void buttonPressed(ButtonPress p) {
        states.peek().buttonPressed(p);
    }

    public GameState getCurrentState() {
        return states.peek();
    }

    public GameSettings getGameSettings() {
        return settings;
    }
}
