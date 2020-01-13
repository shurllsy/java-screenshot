package com.shzq.screenshot.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author lianbo.zhang
 * @date 2020/1/6
 */
public class DragAdapter extends MouseAdapter {

    private int oldX;
    private int oldY;
    //这两个坐标为组件当前的坐标
    private int startX, startY;

    @Override
    public void mousePressed(MouseEvent e) {
        //此为得到事件源组件
        Component cp = (Component) e.getSource();
        //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
        startX = cp.getX();
        startY = cp.getY();
        oldX = e.getXOnScreen();
        oldY = e.getYOnScreen();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        Component cp = (Component) e.getSource();
        //拖动的时候记录新坐标
        //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
        int newX = e.getXOnScreen();
        int newY = e.getYOnScreen();
        //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
        cp.setBounds(startX + (newX - oldX), startY + (newY - oldY), cp.getWidth(), cp.getHeight());
    }
}
