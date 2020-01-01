package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
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
    public void draw(Graphics g) {
        BufferedImage ipImg = imagePanel.appliedImage;
        bufferedImage = PainterUtil.createCompatibleImage(ipImg.getWidth(), ipImg.getHeight(), ipImg.getType());
        Graphics bufferedImgGraphics = bufferedImage.getGraphics();
        bufferedImgGraphics.drawImage(ipImg, 0, 0, null);

        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        Graphics selectAreaGraphics = selectAreaImage.createGraphics();
        selectAreaGraphics.drawImage(imagePanel.selectAreaImageCache, 0, 0, null);

        selectAreaGraphics.setColor(Color.red);
        PainterUtil.drawLine(pressedPoint, draggedPoint, selectAreaGraphics);

        // 防止覆盖左和上边线，裁剪一个像素
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        MyRectangle fixed = new MyRectangle(selectedRectangle);
        fixed.incrementStartX(1);
        fixed.incrementStartY(1);
        BufferedImage subImage = selectAreaImage.getSubimage(1, 1, selectAreaImage.getWidth() - 1, selectAreaImage.getHeight() - 1);

        PainterUtil.drawImage(fixed, subImage, bufferedImgGraphics);

        g.drawImage(bufferedImage, 0, 0, parent.winDi.width, parent.winDi.height, null);
    }

    @Override
    public void released(MouseEvent e) {
        Graphics graphics = imagePanel.selectAreaImageCache.getGraphics();
        graphics.drawImage(imagePanel.selectAreaImage, 0, 0, null);
        applyBufferImage();
    }

}
