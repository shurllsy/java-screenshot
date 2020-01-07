package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 矩形标注
 *
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
        imagePanel.repaint();
    }

    @Override
    public void drawImg(Graphics bufferImageGraphics) {
        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        Graphics selectAreaGraphics = selectAreaImage.createGraphics();
        selectAreaGraphics.drawImage(imagePanel.selectAreaImageCache, 0, 0, null);

        selectAreaGraphics.setColor(Color.red);
        PainterUtil.drawRectangle(rectangle, selectAreaGraphics);

        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        PainterUtil.drawImage(selectedRectangle, selectAreaImage, bufferImageGraphics);
    }

    @Override
    public void released(MouseEvent e) {
        Graphics graphics = imagePanel.selectAreaImageCache.getGraphics();
        graphics.drawImage(imagePanel.selectAreaImage, 0, 0, null);
        graphics.dispose();
        applyBufferImage();
    }
}
