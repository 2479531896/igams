package com.matridx.igams.common.dao.entities;

public class PictureFloat {

    // 文件路径
    private String wjlj;
    // 文件宽度
    private int width;
    // 文件高度
    private int height;
    // 左边距
    private int left;
    // 上边距
    private int top;

    public PictureFloat(String wjlj, int width, int height, int left, int top) {
        this.wjlj = wjlj;
        this.width = width;
        this.height = height;
        this.left = left;
        this.top = top;
    }

    public String getWjlj() {
        return wjlj;
    }
    public void setWjlj(String wjlj) {
        this.wjlj = wjlj;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getLeft() {
        return left;
    }
    public void setLeft(int left) {
        this.left = left;
    }
    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }

}