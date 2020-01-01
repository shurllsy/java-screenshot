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
 * @date 2019/12/28
 */
public class RectanglePainter extends Painter {
    private MyRectangle rectangle = new MyRectangle();
    private Point pressedPoint = new Point();

    public RectanglePainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
        BufferedImage appliedImage = imagePanel.appliedImage;
        bufferedImage = new BufferedImage(appliedImage.getWidth(), appliedImage.getHeight(), appliedImage.getType());
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

    public void draw(Graphics g) {
        /*
            .标注先画到selectAreaImage
            .selectAreaImage填充到bufferImage
            .bufferImage填充到panel

            .selectAreaImageCache缓存了selectAreaImage
            .selectAreaImage为当前的直接目标。draw之前先恢复屏幕的图，用来擦除轨迹，显示最新结果

            .appliedImage缓存了当前全屏显示的图
            .bufferImage 为当前画笔操作的图，draw之前先恢复屏幕的图，用来擦除轨迹，显示最新结果
         */
        BufferedImage ipImg = imagePanel.appliedImage;
        bufferedImage = new BufferedImage(ipImg.getWidth(), ipImg.getHeight(), ipImg.getType());
        bufferedImage.setData(ipImg.getData());

        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        selectAreaImage.setData(imagePanel.selectAreaImageCache.getData());

        Graphics bufferedImgGraphics = bufferedImage.getGraphics();

        Graphics selectAreaGraphics = imagePanel.selectAreaGraphics;
        selectAreaGraphics.setColor(Color.red);
        PainterUtil.drawRectangle(rectangle, selectAreaGraphics);

        MyRectangle selectedRectangle = imagePanel.selectedRectangle;
        MyRectangle fixed = new MyRectangle(selectedRectangle);
        fixed.incrementStartX(1);
        fixed.incrementStartY(1);
        BufferedImage subImage = selectAreaImage.getSubimage(1, 1, selectAreaImage.getWidth()-1, selectAreaImage.getHeight()-1);
        PainterUtil.drawImage(fixed, subImage, bufferedImgGraphics);

        g.drawImage(bufferedImage, 0, 0, parent.winDi.width, parent.winDi.height, null);
    }

    @Override
    public void released(MouseEvent e) {
        imagePanel.selectAreaImageCache.setData(imagePanel.selectAreaImage.getData());
        applyBufferImage();
    }
}
