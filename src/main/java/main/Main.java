package main;

import javax.swing.*;

/*
    TODO: Añadir sonido
*/

public class Main {

    public static void main (String[] args) {
        GameLauncher launcher = new GameLauncher();
        launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launcher.setLocationRelativeTo(null);
        launcher.setVisible(true);
    }
}