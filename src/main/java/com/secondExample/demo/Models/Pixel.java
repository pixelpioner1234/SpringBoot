package com.secondExample.demo.Models;

public class Pixel {
    public int x;
    public int y;
    public final int width = 16;
    public final int height = 16;
    public Color color;
    public User author;

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public java.awt.Color getCustomColor(){
        return switch (this.color) {
            case RED -> java.awt.Color.RED;
            case BLUE -> java.awt.Color.BLUE;
            case WHITE -> java.awt.Color.WHITE;
            case YELLOW -> java.awt.Color.YELLOW;
        };
    }


    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", author=" + author +
                '}';
    }
}
