package gamestates;

import tetris.ButtonPress;
import tetris.GameSettings;

import java.awt.*;
import java.util.Stack;

public class GameStateManager {

    private final Stack<GameState> states;

    public GameStateManager(GameSettings settings) {
        states = new Stack<>();
        push(new PlayingState(this, settings));
        push(new WaitingState(this, states.peek(), settings.fps() / 2));   // Medio segundo de pausa al iniciar el juego
    }

    public void push(GameState state) {
        states.push(state);
        states.peek().entering();
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
}
