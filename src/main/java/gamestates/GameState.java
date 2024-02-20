package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;

public interface GameState {
    void entering();
    void update();
    void draw(Graphics2D g);
    void exiting();
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}
