package com.shzq.screenshot.view;

import com.shzq.screenshot.listener.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class ScreenFrame extends JFrame {

    private ImageBufferPanel imagePanel;
    // 画笔
    private Painter painter;

    public Dimension winDi;
    private ToolsBar toolsBar;

    public ScreenFrame() throws AWTException {
        setUndecorated(true);
        //设置背景透明 防止绘图太慢导致闪一下
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        Robot ro = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit();
        winDi = tk.getScreenSize();
        Rectangle rec = new Rectangle(0, 0, winDi.width, winDi.height);
        BufferedImage initBufferImage = ro.createScreenCapture(rec);
        imagePanel = new ImageBufferPanel(this, initBufferImage);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        setSize(winDi);
        // 初始化工具条
        toolsBar = new ToolsBar(this);
        setVisible(true);
        //防止窗口被状态栏挤下去
        setBounds(0, 0, winDi.width, winDi.height);
    }

    public void setPainter(Painter painter) {
        Arrays.stream(imagePanel.getMouseListeners())
                .forEach(imagePanel::removeMouseListener);
        Arrays.stream(imagePanel.getMouseMotionListeners())
                .forEach(imagePanel::removeMouseMotionListener);

        imagePanel.addMouseListener(painter);
        imagePanel.addMouseMotionListener(painter);
        painter.applyBufferImage();
        this.painter = painter;
    }

    @Override
    public void dispose() {
        super.dispose();
        toolsBar.dispose();
    }

    public BufferedImage getSelectedImg() {
        return imagePanel.getSelectedImg();
    }

    public ImageBufferPanel getImagePanel() {
        return imagePanel;
    }

    public ToolsBar getToolsBar() {
        return toolsBar;
    }

    public Painter getPainter() {
        return painter;
    }
}