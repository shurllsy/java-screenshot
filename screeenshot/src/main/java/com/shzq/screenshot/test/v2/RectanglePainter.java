package com.shzq.screenshot.test.v2;

import com.shzq.screenshot.bean.MyRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author lianbo.zhang
 * @date 2019/12/28
 */
public class RectanglePainter implements MouseListener, MouseMotionListener {
    private MyRectangle rectangle = new MyRectangle();
    private Point pressedPoint = new Point();
    private JPanel jPanel;
    Graphics g;
    private int preX = -1;
    private int preY = -1;
    public RectanglePainter(JPanel jPanel, Graphics g) {
        this.jPanel = jPanel;
        this.g = g;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressedPoint.setLocation(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        rectangle.reset(pressedPoint, e.getPoint());
        g.setColor(Color.red);
        g.drawRect(rectangle.getStartX(), rectangle.getStartY(), rectangle.getDimension().width,
                rectangle.getDimension().height);
//        if (preX > 0 && preY > 0) {
//            g.setColor(Color.black);
//            g.drawLine(preX, preY, e.getX(), e.getY());
//        }
//        preX = e.getX();
//        preY = e.getY();
        jPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
