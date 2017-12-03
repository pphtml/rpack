package org.superbiz.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Dot {
    private final int x;
    private final int y;
    private final Color c;
    private final int l;

    public Dot(int x, int y, Color c, int l) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.l = l;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Dot{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", c=").append(c);
        sb.append(", l=").append(l);
        sb.append('}');
        return sb.toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getC() {
        return c;
    }

    public int getL() {
        return l;
    }

    public static Dot create(int x, int y, Color c, int l) {
        return new Dot(x, y, c, l);
    }

    @JsonIgnore
    public String getKey() {
        return String.format("%d_%d", x, y);
    }

    enum Color { red, green, blue }
}
