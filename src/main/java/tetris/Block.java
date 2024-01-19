package tetris;

import java.awt.*;

public class Block {

    public int x;
    public int y;
    public Color color;
    public static int size;

    public Block(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void draw(Graphics2D g) {

        g.setColor(color.brighter());
        Polygon triangle = new Polygon();
        triangle.addPoint(x,y);
        triangle.addPoint(x, y+size);
        triangle.addPoint(x+size, y);
        g.fillPolygon(triangle);

        g.setColor(color.darker());
        triangle = new Polygon();
        triangle.addPoint(x+size, y+size);
        triangle.addPoint(x, y+size);
        triangle.addPoint(x+size, y);
        g.fillPolygon(triangle);

        int centerX = x + size/4;
        int centerY = y + size/4;
        g.setColor(color);
        g.fillRect(centerX, centerY, size/2, size/2);
    }
}
