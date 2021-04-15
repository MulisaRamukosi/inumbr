package com.puzzle.industries.inumbr.dataModels;

public class Ball {

    private final int num;
    private boolean selected;

    public Ball(int num) {
        this.num = num;
    }

    public Ball(int num, boolean selected) {
        this.num = num;
        this.selected = selected;
    }

    public int getNum() {
        return num;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
