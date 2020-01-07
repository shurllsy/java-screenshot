package com.shzq.screenshot.utils;

import com.shzq.screenshot.bean.MyRectangle;

import javax.swing.*;
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

    // 创建硬件适配的缓冲图像，为了能显示得更快速
    public static BufferedImage createCompatibleImage(int w, int h, int type) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        return gc.createCompatibleImage(w, h, type);
    }

    /***
     * 将控件转换为BufferImage
     * @param ta 目标控件
     */
    public static BufferedImage componentToImage(JComponent ta) {
        BufferedImage img = new BufferedImage(ta.getWidth(), ta.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        ta.printAll(g2d);
        g2d.dispose();
        return img;
    }

}
