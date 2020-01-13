package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.Result;
import com.shzq.screenshot.listener.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class ScreenFrame extends JFrame {

    private ImageBufferPanel imagePanel;
    // 选择区域图层
    public SelectAreaPanel selectAreaPanel;
    // 画笔
    private Painter painter;

    public Dimension winDi;
    private ToolsBar toolsBar;
    private Consumer<Result>[] consumers;

    @SafeVarargs
    public ScreenFrame(Consumer<Result>... consumer) throws AWTException {
        consumers = consumer;
        setUndecorated(true);
        //设置背景透明 防止绘图太慢导致闪一下
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        Robot ro = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit();
        winDi = tk.getScreenSize();
        setSize(winDi);
        BufferedImage initBufferImage = ro.createScreenCapture(new Rectangle(winDi.width, winDi.height));

        // 背景 灰度图
        JLabel backgroundLabel = new JLabel();
        RescaleOp rescaleOp = new RescaleOp(0.6f, 0, null);
        BufferedImage rescaleImage = rescaleOp.filter(initBufferImage, null);
        backgroundLabel.setIcon(new ImageIcon(rescaleImage));
        backgroundLabel.setBounds(new Rectangle(winDi));

        // 绘画面板
        imagePanel = new ImageBufferPanel(this, initBufferImage);
        imagePanel.setBounds(new Rectangle(winDi));
        // 工具条
        toolsBar = new ToolsBar(this);
        // 选择区域
        selectAreaPanel = new SelectAreaPanel(imagePanel);

        JPanel panel = new JPanel(true);
        panel.setLayout(null);
        getContentPane().add(panel, BorderLayout.CENTER);

        panel.add(toolsBar);
        panel.add(selectAreaPanel);
        panel.add(imagePanel);
        panel.add(backgroundLabel);


        setVisible(true);
        //防止窗口被状态栏挤下去
        setLocation(0, 0);
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

    public void consume(Result result) {
        Arrays.asList(consumers).forEach(consume -> {
            consume.accept(result);
        });
    }

}