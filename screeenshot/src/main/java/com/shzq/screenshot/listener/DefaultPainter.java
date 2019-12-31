package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.enums.States;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class DefaultPainter extends Painter {
    private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);//表示一般情况下的鼠标状态

    private Point pressedPoint = new Point();
    //下面四个常量,分别表示谁是被选中的那条线上的端点
    public static final int START_X = 1;
    public static final int START_Y = 2;
    public static final int END_X = 3;
    public static final int END_Y = 4;

    //当前被选中的X和Y,只有这两个需要改变
    public int currentX, currentY;

    public DefaultPainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
    }

    @Override
    public void pressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            pressedPoint.setLocation(e.getX(), e.getY());
            tools.setVisible(false);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            parent.dispose();
        }
    }

    @Override
    public void released(MouseEvent e) {
        // 设置工具条位置
        MyRectangle outRectangle = imagePanel.getSelectedRectangle();
//        MyRectangle t = new MyRectangle(2,2, 20,20);
//        PainterUtil.drawRectangle(t, imagePanel.selectAreaGraphics.create());
//        imagePanel.repaint();
        tools.moveTo(tools.getToolsLocationPoint(outRectangle));
        if (e.isPopupTrigger()) {
            if (imagePanel.getCurrent() == States.MOVE) {
                outRectangle.setStartX(0);
                outRectangle.setStartY(0);
                outRectangle.setEndX(0);
                outRectangle.setEndY(0);
                imagePanel.repaint();
            } else {
                parent.dispose();
            }
        }
    }

    @Override
    public void clicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();
            if (imagePanel.getSelect().contains(p)) {
                BufferedImage selectedImg = imagePanel.getSelectedImg();
                tools.copyToClipboard(selectedImg);
                parent.dispose();
            }
        }
    }

    @Override
    public void moved(MouseEvent e) {
        doMouseMoved(e);
        initSelect(imagePanel.current);
    }

    @Override
    public void dragged(MouseEvent e) {
        MyRectangle outRectangle = imagePanel.getSelectedRectangle();
        int x = e.getX();
        int y = e.getY();
        // 鼠标在X轴拖动距离，往右为正，往左为负
        int movedX = x - pressedPoint.x;
        // 鼠标在Y轴拖动的距离，往下为正，往上为负
        int movedY = y - pressedPoint.y;
        States current = imagePanel.current;
        if (current == States.MOVE) {

            //控制选框只能在屏幕范围内拖动，并且保持大小不异常改变
            int tmpStartX = outRectangle.getStartX() + movedX;
            int tmpStartY = outRectangle.getStartY() + movedY;
            int tmpEndX = outRectangle.getEndX() + movedX;
            int tmpEndY = outRectangle.getEndY() + movedY;
            if (tmpStartX >= 0 && tmpEndX <= parent.winDi.width) {
                outRectangle.setStartX(tmpStartX);
                outRectangle.setEndX(tmpEndX);
            }
            if (tmpStartY >= 0 && tmpEndY <= parent.winDi.height) {
                outRectangle.setStartY(tmpStartY);
                outRectangle.setEndY(tmpEndY);
            }

            pressedPoint.setLocation(e.getPoint());
        } else if (current == States.EAST || current == States.WEST) {
            if (currentX == START_X) {
                outRectangle.incrementStartX(movedX);
            } else {
                outRectangle.incrementEndX(movedX);
            }
            pressedPoint.x = x;
        } else if (current == States.NORTH || current == States.SOUTH) {
            if (currentY == START_Y) {
                outRectangle.incrementStartY(movedY);
            } else {
                outRectangle.incrementEndY(movedY);
            }
            pressedPoint.y = y;
        } else if (current == States.NORTH_WEST || current == States.NORTH_EAST ||
                current == States.SOUTH_EAST || current == States.SOUTH_WEST) {
            if (currentY == START_Y) {
                outRectangle.incrementStartY(movedY);
            } else {
                outRectangle.incrementEndY(movedY);
            }
            if (currentX == START_X) {
                outRectangle.incrementStartX(movedX);
            } else {
                outRectangle.incrementEndX(movedX);
            }
            pressedPoint.setLocation(e.getPoint());
        } else {
            outRectangle.reset(pressedPoint.x, pressedPoint.y, e.getX(), e.getY());
        }
        imagePanel.repaint();

    }

    //特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
    private void doMouseMoved(MouseEvent e) {
        //判断编辑后不能再移动
        Rectangle[] rec = imagePanel.getRec();
        if (imagePanel.getSelect().contains(e.getPoint())) {
            imagePanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            imagePanel.current = States.MOVE;
        } else {
            States[] st = States.values();
            for (int i = 0; i < rec.length; i++) {
                if (rec[i].contains(e.getPoint())) {
                    imagePanel.current = st[i];
                    imagePanel.setCursor(st[i].getCursor());
                    return;
                }
            }
            imagePanel.setCursor(cs);
            imagePanel.current = States.DEFAULT;
        }
    }

    //根据东南西北等八个方向决定选中的要修改的X和Y的座标
    private void initSelect(States state) {
        switch (state) {
            case EAST:
                currentX = END_X;
                currentY = 0;
                break;
            case WEST:
                currentX = START_X;
                currentY = 0;
                break;
            case NORTH:
                currentX = 0;
                currentY = START_Y;
                break;
            case SOUTH:
                currentX = 0;
                currentY = END_Y;
                break;
            case NORTH_EAST:
                currentX = END_X;
                currentY = START_Y;
                break;
            case NORTH_WEST:
                currentX = START_X;
                currentY = START_Y;
                break;
            case SOUTH_EAST:
                currentX = END_X;
                currentY = END_Y;
                break;
            case SOUTH_WEST:
                currentX = START_X;
                currentY = END_Y;
                break;
            default:
                currentX = 0;
                currentY = 0;
                break;
        }
    }
}
