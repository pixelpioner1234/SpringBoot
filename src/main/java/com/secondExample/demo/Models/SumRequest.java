package com.secondExample.demo.Models;

public class SumRequest<T> {

    public T a;
    public T b;

    public SumRequest(T a, T b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return a;
    }

    public void setA(T a) {
        this.a = a;
    }

    public T getB() {
        return b;
    }

    public void setB(T b) {
        this.b = b;
    }
}
