package tetris;

import java.awt.*;

public record GameSettings(
        int width,
        int height,
        long seed,
        int fps,
        Color[] palette,
        int[] keyBindings
) {}
