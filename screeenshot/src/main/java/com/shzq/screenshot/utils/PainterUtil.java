package com.shzq.screenshot.utils;

import com.shzq.screenshot.bean.MyRectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class PainterUtil {

    public static void drawImage(MyRectangle rectangle, BufferedImage image, Graphics g) {
        g.drawImage(image, rectangle.getStartX(), rectangle.getStartY(), null);
    }

    /**
     * 画矩形
     *
     * @param rectangle 矩形
     * @param g         Graphics 画布画笔
     */
    public static void drawRectangle(MyRectangle rectangle, Graphics g) {
        Point leftTop = rectangle.getLeftTop();
        Point leftBottom = rectangle.getLeftBottom();
        Point rightTop = rectangle.getRightTop();
        Point rightBottom = rectangle.getRightBottom();

        drawLine(leftTop, rightTop, g);
        drawLine(rightTop, rightBottom, g);
        drawLine(rightBottom, leftBottom, g);
        drawLine(leftBottom, leftTop, g);
    }

    /**
     * 画线
     *
     * @param g  Graphics 画布画笔
     * @param p1 线端点1
     * @param p2 线端点2
     */
    public static void drawLine(Point p1, Point p2, Graphics g) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

}
