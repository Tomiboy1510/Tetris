package tetris;

public record GameSettings(
        int WIDTH,
        int HEIGHT,
        long seed,
        int fps
) {}
