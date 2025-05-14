package tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tetromino {
    private static final int ROWS = 20;
    private static final int COLS = 10;

    private final Point[] points;
    private final Color color;

    private Tetromino(Point[] p, Color c) {
        this.points = p;
        this.color = c;
    }

    public static Tetromino random() {
        List<Tetromino> shapes = new ArrayList<>();
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1)}, Color.YELLOW)); // O
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(-1,0), new Point(1,0), new Point(2,0)}, Color.CYAN));  // I
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(-1,0), new Point(0,1), new Point(1,1)}, Color.GREEN)); // S
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(1,0), new Point(0,1), new Point(-1,1)}, Color.RED));   // Z
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(-1,0), new Point(1,0), new Point(-1,1)}, Color.BLUE)); // J
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(-1,0), new Point(1,0), new Point(1,1)}, Color.ORANGE)); // L
        shapes.add(new Tetromino(new Point[]{new Point(0,0), new Point(-1,0), new Point(1,0), new Point(0,1)}, Color.MAGENTA)); // T

        Tetromino t = shapes.get(new Random().nextInt(shapes.size()));
        Point[] pts = new Point[4];
        for (int i = 0; i < 4; i++) {
            pts[i] = new Point(t.points[i].x + COLS/2, t.points[i].y);
        }
        return new Tetromino(pts, t.color);
    }

    public Point[] getPoints() { return points; }
    public Color getColor() { return color; }

    public boolean canMove(int dr, int dc, Color[][] board) {
        for (Point p : points) {
            int nr = p.y + dr;
            int nc = p.x + dc;
            if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) return false;
            if (board[nr][nc] != null) return false;
        }
        return true;
    }

    public void move(int dr, int dc) {
        for (Point p : points) p.translate(dc, dr);
    }

    public boolean canRotate(Color[][] board) {
        Point center = points[0];
        for (Point p : points) {
            int nr = center.y + (p.x - center.x);
            int nc = center.x - (p.y - center.y);
            if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) return false;
            if (board[nr][nc] != null) return false;
        }
        return true;
    }

    public void rotate() {
        Point center = points[0];
        for (Point p : points) {
            int x = p.x - center.x;
            int y = p.y - center.y;
            p.x = center.x - y;
            p.y = center.y + x;
        }
    }
}
