package com.noam.goren.technical.model;

import javax.persistence.*;

@Entity
public class Point {

    @Id
    @GeneratedValue
    private long id;
    private int x;
    private int y;
    private boolean isDirtPatch;

    public Point() {}

    public Point(final int x, final int y, final boolean isDirtPatch) {
        this.x = x;
        this.y = y;
        this.isDirtPatch = isDirtPatch;
    }

    public Point(final Point point) {
        this(point.getX(), point.getY(), point.isDirtPatch);
    }

    public Point(final int x, final int y) {
        this(x, y, false);
    }


    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public boolean isDirtPatch() {
        return isDirtPatch;
    }

    @Override
    public boolean equals(final Object o) {
        if(!(o instanceof Point))
            return false;

        final Point coordinate = (Point)o;
        return (this.x == coordinate.getX()) && (this.y == coordinate.getY());
    }
}
