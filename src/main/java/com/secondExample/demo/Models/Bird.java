package com.secondExample.demo.Models;

public class Bird {

    public int id;
    public String name;
    public String color;


    public Bird() {}
    public Bird( String name, String color) {
        this.name = name;
        this.color = color;
    }
    public Bird(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
