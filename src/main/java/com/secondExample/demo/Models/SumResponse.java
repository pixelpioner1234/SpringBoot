package com.secondExample.demo.Models;

public class SumResponse<T> {
    public T result;

    public SumResponse(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
