package com.shzq.screenshot.view.component;

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

    public ToolsButton(ToolsBar toolsBar, String icon) {
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
        this.removeMouseListener(this);
        // 点击，清除其他按钮的点击状态
        List<ToolsButton> tools = toolsBar.getTools();
        tools.stream()
                .filter(tb -> tb != this)
                .forEach(tb -> {
                    tb.mouseExited();
                    tb.removeMouseListener(tb);
                    tb.addMouseListener(tb);
                });
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
}
