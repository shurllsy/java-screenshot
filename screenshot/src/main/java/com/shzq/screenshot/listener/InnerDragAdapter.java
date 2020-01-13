package com.shzq.screenshot.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 只能在parent容器范围内拖动
 *
 * @author lianbo.zhang
 * @date 2020/1/12
 */
public class InnerDragAdapter extends MouseAdapter {

    private final Component parent;
    private Consumer<Component>[] consumers;

    //这两个坐标为组件当前的坐标
    private int startX, startY;
    // 鼠标摁下，在屏幕上的坐标
    private Point pressedPoint;

    @SafeVarargs
    public InnerDragAdapter(Component parent, Consumer<Component>... consumers) {
        this.parent = parent;
        this.consumers = consumers;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //此为得到事件源组件
        Component cp = (Component) e.getSource();
        //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
        startX = cp.getX();
        startY = cp.getY();
        pressedPoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component cp = (Component) e.getSource();
        //拖动的时候记录新坐标
        //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
        int newX = e.getXOnScreen();
        int newY = e.getYOnScreen();
        int oldX = pressedPoint.x;
        int oldY = pressedPoint.y;

        int boundsX = startX + (newX - oldX);
        int boundsY = startY + (newY - oldY);
        boundsX = Math.max(boundsX, 0);
        boundsY = Math.max(boundsY, 0);
        boundsX = Math.min(boundsX, parent.getWidth() - cp.getWidth());
        boundsY = Math.min(boundsY, parent.getHeight() - cp.getHeight());

        //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
        cp.setBounds(boundsX, boundsY, cp.getWidth(), cp.getHeight());
        Arrays.stream(consumers).forEach(consumers -> consumers.accept(cp));
    }
}