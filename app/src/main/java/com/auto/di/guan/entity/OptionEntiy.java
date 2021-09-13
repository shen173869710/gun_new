package com.auto.di.guan.entity;

public class OptionEntiy implements Comparable{
    private int name;
    private boolean sel;

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isSel() {
        return sel;
    }

    public void setSel(boolean sel) {
        this.sel = sel;
    }

    public OptionEntiy(int name, boolean sel) {
        this.name = name;
        this.sel = sel;
    }

    @Override
    public int compareTo(Object o) {
        return this.name - ((OptionEntiy)o).name;
    }
}
