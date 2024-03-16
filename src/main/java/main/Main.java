package main;

import javax.swing.*;
import java.awt.*;

/*
    TODO:
        - Añadir sonido
        - Javadoc
        - GameLauncher con: keybindings, resolución, semilla
*/

public class Main {

    public static void main (String[] args) {
        GameLauncher launcher = new GameLauncher();
        launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launcher.setLocationRelativeTo(null);
        launcher.setVisible(true);
    }
}