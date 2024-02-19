package tetris;

import java.awt.*;

public class Block {

    public int x;
    public int y;
    public Color color;
    public static int SIZE;

    public Block(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void draw(Graphics2D g) {

        g.setColor(color.brighter());
        Polygon triangle = new Polygon();
        triangle.addPoint(x,y);
        triangle.addPoint(x, y+ SIZE);
        triangle.addPoint(x+ SIZE, y);
        g.fillPolygon(triangle);

        g.setColor(color.darker());
        triangle = new Polygon();
        triangle.addPoint(x+ SIZE, y+ SIZE);
        triangle.addPoint(x, y+ SIZE);
        triangle.addPoint(x+ SIZE, y);
        g.fillPolygon(triangle);

        int centerX = x + SIZE /4;
        int centerY = y + SIZE /4;
        g.setColor(color);
        g.fillRect(centerX, centerY, SIZE /2, SIZE /2);
    }
}
