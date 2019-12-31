package com.shzq.screenshot.listener;

import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author lianbo.zhang
 * @date 2019/12/28
 */
public class RectanglePainter extends Painter {
    private MyRectangle rectangle = new MyRectangle();
    private Point pressedPoint = new Point();

    public RectanglePainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
    }

    @Override
    public void pressed(MouseEvent e) {
        tools.setVisible(true);

        pressedPoint.setLocation(e.getPoint());
    }

    @Override
    public void dragged(MouseEvent e) {
        rectangle.reset(pressedPoint, e.getPoint());
        int moveX = Math.negateExact(imagePanel.getSelectedRectangle().getStartX());
        int moveY = Math.negateExact(imagePanel.getSelectedRectangle().getStartY());
        rectangle.incrementMove(moveX, moveY);
        draw();
    }

    public void draw() {
        Graphics graphics = imagePanel.selectAreaGraphics.create();
        graphics.setColor(Color.RED);
        imagePanel.selectAreaImage.setData(imagePanel.selectAreaImageCache.getData());
        System.out.println("selectedRectangle() = " + imagePanel.getSelectedRectangle());
        System.out.println("rectangle = " + rectangle);
        PainterUtil.drawRectangle(rectangle, graphics);
        imagePanel.refreshSelectArea();
//        imagePanel.repaint();
    }

    @Override
    public void released(MouseEvent e) {
        imagePanel.selectAreaImageCache.setData(imagePanel.selectAreaImage.getData());
    }
}
