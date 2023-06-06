package com.oop.model;

public abstract class Model {
    protected String ten;
    protected Model(String name) {
        this.ten = name;
    }
    public String getTen() {
        return ten;
    }
}
