package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.listener.InnerDragAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * @author lianbo.zhang
 * @date 2020/1/12
 */
public class SelectAreaPanel extends JPanel {
    private ImageBufferPanel imagePanel;

    public SelectAreaPanel(ImageBufferPanel imagePanel) {
        this.imagePanel = imagePanel;
        setDoubleBuffered(true);
        InnerDragAdapter dragAdapter = new InnerDragAdapter(imagePanel, new MoveConsumer());
        addMouseMotionListener(dragAdapter);
        addMouseListener(dragAdapter);
        addMouseListener(new Release());
//        int a= 5;
//        Border border = BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.decode("#1EA4FF")),
//                BorderFactory.createEmptyBorder(a,a,a,a));
        setBorder(BorderFactory.createLineBorder(Color.decode("#1EA4FF")));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        int startX = selectedRectangle.getStartX();
        int startY = selectedRectangle.getStartY();
        Dimension dimension = selectedRectangle.getDimension();
        BufferedImage subImage = imagePanel.image.getSubimage(startX, startY, dimension.width, dimension.height);
        g.drawImage(subImage, 0, 0, null);
        g.dispose();
    }

    /**
     * 移动回调
     */
    private class MoveConsumer implements Consumer<Component> {

        @Override
        public void accept(Component component) {
            imagePanel.getParent().getToolsBar().setVisible(false);
            MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
            int startX = component.getX();
            int startY = component.getY();
            int endX = startX + component.getWidth();
            int endY = startY + component.getHeight();
            selectedRectangle.reset(startX, startY, endX, endY);
            imagePanel.setSelect(selectedRectangle.toRectangle());
        }
    }

    private class Release extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            ToolsBar toolsBar = imagePanel.getParent().getToolsBar();
            MyRectangle outRectangle = imagePanel.getSelectedRectangle();
            toolsBar.moveTo(toolsBar.getToolsLocationPoint(outRectangle));
        }
    }
}
