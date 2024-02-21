package gamestates;

import tetris.ButtonPress;

import java.awt.*;

public interface GameState {
    void entering();
    void update();
    void draw(Graphics2D g);
    void exiting();
    void buttonPressed(ButtonPress p);
}
