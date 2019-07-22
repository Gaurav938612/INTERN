package com.example.nit_project.ClickableActivity;

public class ClickableArea<T> {

    private int x;
    private int y;
    private int w;
    private int h;
    private String index;

    private String item;

    public ClickableArea(int x, int y, int w, int h, String item, String index){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.item = item;
        this.index=index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getItem() {
        return item;
    }

    public void setLabel(String item) {
        this.item = item;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
