package com.shzq.screenshot.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author lianbo.zhang
 * @date 2020/1/6
 */
public class DragAdapter extends MouseAdapter {
    Point press = new Point();
    private Component component;

    public DragAdapter(Component component) {
        this.component = component;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        press.setLocation(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        // 鼠标在X轴拖动距离，往右为正，往左为负
        int movedX = e.getX() - press.x;
        // 鼠标在Y轴拖动的距离，往下为正，往上为负
        int movedY = e.getY() - press.y;
        Point location = component.getLocation();
        location.x += movedX;
        location.y += movedY;
        component.setLocation(location);
    }
}
