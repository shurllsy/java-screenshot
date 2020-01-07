package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 直线标注
 *
 * @author lianbo.zhang
 * @date 2019/12/30
 */
public class LinePainter extends Painter {

    private Point pressedPoint = new Point();
    private Point draggedPoint = new Point();

    public LinePainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
    }

    @Override
    public void pressed(MouseEvent e) {
        tools.setVisible(true);
        pressedPoint.setLocation(e.getPoint());
        pressedPoint.x -= imagePanel.getSelectedRectangle().getStartX();
        pressedPoint.y -= imagePanel.getSelectedRectangle().getStartY();
    }

    @Override
    public void dragged(MouseEvent e) {
        draggedPoint.setLocation(e.getPoint());
        draggedPoint.x -= imagePanel.getSelectedRectangle().getStartX();
        draggedPoint.y -= imagePanel.getSelectedRectangle().getStartY();
        imagePanel.repaint();
    }

    @Override
    public void drawImg(Graphics bufferImageGraphics) {
        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        Graphics selectAreaGraphics = selectAreaImage.createGraphics();
        selectAreaGraphics.drawImage(imagePanel.selectAreaImageCache, 0, 0, null);

        selectAreaGraphics.setColor(Color.red);
        PainterUtil.drawLine(pressedPoint, draggedPoint, selectAreaGraphics);

        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        PainterUtil.drawImage(selectedRectangle, selectAreaImage, bufferImageGraphics);

        selectAreaGraphics.dispose();
        applyBufferImage();
    }

    @Override
    public void released(MouseEvent e) {
        Graphics graphics = imagePanel.selectAreaImageCache.getGraphics();
        graphics.drawImage(imagePanel.selectAreaImage, 0, 0, null);
        graphics.dispose();
    }

}
