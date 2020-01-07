package com.shzq.screenshot.view.component;

import com.shzq.screenshot.listener.Painter;
import com.shzq.screenshot.view.ToolsBar;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * @author lianbo.zhang
 * @date 2020/1/1
 */
public class ToolsButton extends JButton implements MouseListener {

    private static final String ICON_PATH = "/icon/";
    private static final String SUFFIX = ".png";
    private ToolsBar toolsBar;

    private ImageIcon normalImg;
    private ImageIcon highlightImg;

    private Painter painter;

    public ToolsButton(ToolsBar toolsBar, String icon, Painter painter) {
        this.painter = painter;
        this.toolsBar = toolsBar;
        this.addMouseListener(this);
        normalImg = new ImageIcon(getClass().getResource(ICON_PATH + icon + SUFFIX));
        highlightImg = new ImageIcon(getClass().getResource(ICON_PATH + icon + "Highlight" + SUFFIX));
        setIcon(normalImg);
        setBorder(null);
        setContentAreaFilled(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        setIcon(highlightImg);
        setBorder(null);
        List<ToolsButton> tools = toolsBar.getTools();
        for (ToolsButton tool : tools) {
            if (tool == this) {
                tool.activated();
                tool.painter.activate();
            } else {
                tool.unactivated();
                tool.painter.inactivate();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEntered();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseExited();
    }

    public void mouseEntered() {
        setIcon(highlightImg);
        setBorder(null);
    }

    public void mouseExited() {
        setIcon(normalImg);
        setBorder(null);
    }

    /**
     * 被激活，选中时触发
     */
    protected void activated() {
        mouseEntered();
        removeMouseListener(this);
    }

    /**
     * 被取消激活，被取消选中时触发
     */
    protected void unactivated() {
        addMouseListener(this);
        mouseExited();
    }
}
