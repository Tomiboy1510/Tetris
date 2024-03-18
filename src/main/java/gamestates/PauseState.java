package gamestates;

import main.SoundManager;
import tetris.ButtonPress;

import java.awt.*;

public class PauseState implements GameState {

    private final GameStateManager manager;
    private final GameState stateBeneath;

    public PauseState(GameStateManager manager) {
        this.manager = manager;
        this.stateBeneath = manager.getCurrentState();
    }

    @Override
    public void entering() {
        SoundManager.getInstance().playSound("pause.wav");
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public void draw(Graphics2D g) {
        // Render game area
        stateBeneath.draw(g);

        // Show "PAUSE" text
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, manager.getGameSettings().width() / 20));
        g.drawString(
                "PAUSE",
                manager.getGameSettings().width() / 80,
                manager.getGameSettings().width() / 20
        );
    }

    @Override
    public void exiting() {
        SoundManager.getInstance().playSound("unpause.wav");
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
