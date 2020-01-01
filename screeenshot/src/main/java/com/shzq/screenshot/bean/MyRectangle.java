package com.shzq.screenshot.bean;

import java.awt.*;

/**
 * 画线的矩形
 *
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class MyRectangle {


    private Point leftTop = new Point();
    private Point leftBottom = new Point();
    private Point rightTop = new Point();
    private Point rightBottom = new Point();

    public MyRectangle() {

    }

    public MyRectangle(MyRectangle source){
        leftTop.setLocation(source.leftTop);
        leftBottom.setLocation(source.leftBottom);
        rightTop.setLocation(source.rightTop);
        rightBottom.setLocation(source.rightBottom);
    }

    /**
     * 从矩形对角线的两个坐标点创建矩形
     *
     * @param p1 对角线上的点1
     * @param p2 对角线上的点2
     */
    public MyRectangle(Point p1, Point p2) {
        this(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * 矩形对角线的两个坐标点
     *
     * @param startX 对角线上的点1 x坐标
     * @param startY 对角线上的点1 y坐标
     * @param endX   对角线上的点2 x坐标
     * @param endY   对角线上的点3 y坐标
     */
    public MyRectangle(int startX, int startY, int endX, int endY) {
        reset(startX, startY, endX, endY);
    }

    /**
     * 坐标移动时刷新内部数据
     *
     * @param startX 对角线上的点1 x坐标
     * @param startY 对角线上的点1 y坐标
     * @param endX   对角线上的点2 x坐标
     * @param endY   对角线上的点3 y坐标
     */
    public void reset(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int minY = Math.min(startY, endY);
        int maxX = Math.max(startX, endX);
        int maxY = Math.max(startY, endY);

        leftTop.setLocation(minX, minY);

        leftBottom.setLocation(minX, maxY);

        rightTop.setLocation(maxX, minY);

        rightBottom.setLocation(maxX, maxY);
    }

    public int getStartX() {
        return leftTop.x;
    }

    public int getEndX() {

        return rightTop.x;
    }

    public int getStartY() {
        return leftTop.y;
    }

    public int getEndY() {
        return leftBottom.y;
    }

    public void setStartX(int x) {
        leftTop.x = x;
        leftBottom.x = x;
    }

    public void setEndX(int x) {
        rightTop.x = x;
        rightBottom.x = x;
    }

    public void setStartY(int y) {
        leftTop.y = y;
        rightTop.y = y;
    }

    public void setEndY(int y) {
        leftBottom.y = y;
        rightBottom.y = y;
    }

    public void incrementStartX(int value) {
        setStartX(getStartX() + value);
    }

    public void incrementEndX(int value) {
        setEndX(getEndX() + value);
    }

    public void incrementStartY(int value) {
        setStartY(getStartY() + value);
    }

    public void incrementEndY(int value) {
        setEndY(getEndY() + value);
    }

    public void reset(Point p1, Point p2) {
        reset(p1.x, p1.y, p2.x, p2.y);
    }

    public void reset() {
        reset(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y);
    }

    public Point getLeftTop() {
        return leftTop;
    }

    public Dimension getDimension() {
        Dimension d = new Dimension();
        d.setSize(rightTop.x - leftTop.x, leftBottom.y - leftTop.y);
        return d;
    }

    public Rectangle toRectangle() {
        Rectangle r = new Rectangle();
        r.setLocation(leftTop);
        r.setSize(getDimension());
        return r;
    }

    public void setLeftTop(Point leftTop) {
        this.leftTop = leftTop;
    }

    public Point getLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(Point leftBottom) {
        this.leftBottom = leftBottom;
    }

    public Point getRightTop() {
        return rightTop;
    }

    public void setRightTop(Point rightTop) {
        this.rightTop = rightTop;
    }

    public Point getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(Point rightBottom) {
        this.rightBottom = rightBottom;
    }

    /**
     * 递增移动，x在x轴向右移动为正数，y在y轴向下移动为正数
     * @param x x轴移动的距离
     * @param y y轴移动的距离
     */
    public void incrementMove(int x, int y){
        incrementStartX(x);
        incrementEndX(x);
        incrementStartY(y);
        incrementEndY(y);
    }

    @Override
    public String toString() {
        return String.format("MyRectangle : \n(%d,%d)\t%s\t(%d,%d)\n\n%s\n\n(%d,%d)\t\t(%d,%d)",
                leftTop.x, leftTop.y, getDimension().width, rightTop.x, rightTop.y, getDimension().height, leftBottom.x,
                leftBottom.y, rightBottom.x, rightBottom.y);
    }
}
