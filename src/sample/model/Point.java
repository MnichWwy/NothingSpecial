package sample.model;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double implements Comparable<Point> {
    private double angle;
    private double distance;

    public Point(String lebel) {
        super();
        parseString(lebel);
    }
    public Point(double x, double y) {
        super(x, y);
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }


    public int compareTo(Point other) {
        if (this.y != other.getY())
            return java.lang.Double.valueOf(this.y).compareTo(other.getY());
        else
            return java.lang.Double.valueOf(this.x).compareTo(other.getX());
    }

    private void parseString(String lebel) {
        lebel = lebel.trim();
        x = java.lang.Double.valueOf(lebel.substring(lebel.indexOf("(") + 1, lebel.indexOf(",")));
        y = java.lang.Double.valueOf(lebel.substring(lebel.indexOf(",") + 1, lebel.indexOf(")")));
    }

    @Override
    public boolean equals(Object obj) {
        Point p=(Point)obj;
        if(p.x-x>-0.0001 && p.x-x<0.0001 && p.y-y>-0.0001 && p.y-y<0.0001)
            return true;
        else return false;
    }
}