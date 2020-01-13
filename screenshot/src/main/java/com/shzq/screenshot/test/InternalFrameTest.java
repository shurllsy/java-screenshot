package com.shzq.screenshot.test;

import com.shzq.screenshot.listener.DragAdapter;
import com.shzq.screenshot.listener.InnerDragAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class InternalFrameTest extends JFrame {

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                InternalFrameTest frame = new InternalFrameTest();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public InternalFrameTest() throws AWTException {
        setSize(660, 500);
        setLocationRelativeTo(null);

        BufferedImage initBufferImage = new Robot().createScreenCapture(new Rectangle(getWidth(), getHeight()));

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        add(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.GRAY);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon(initBufferImage));
        panel_1.add(jLabel);
        panel_1.setBounds(0, 0, getWidth(), getHeight());

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(Color.RED);
        panel_2.setBounds(261, 85, 130, 130);

        panel.add(panel_2);
        panel.add(panel_1);

        InnerDragAdapter m = new InnerDragAdapter(this);

        panel_2.addMouseListener(m);
        panel_2.addMouseMotionListener(m);

        setUndecorated(true);
        DragAdapter dragAdapter = new DragAdapter();
        this.addMouseMotionListener(dragAdapter);
        this.addMouseListener(dragAdapter);
    }

    // 写一个类继承鼠标监听器的适配器，这样就可以免掉不用的方法。
    class MyListener extends MouseAdapter {
        //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
        int newX, newY, oldX, oldY;
        //这两个坐标为组件当前的坐标
        int startX, startY;

        @Override
        public void mousePressed(MouseEvent e) {
            //此为得到事件源组件
            Component cp = (Component) e.getSource();
            //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
            startX = cp.getX();
            startY = cp.getY();
            oldX = e.getXOnScreen();
            oldY = e.getYOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Component cp = (Component) e.getSource();
            //拖动的时候记录新坐标
            newX = e.getXOnScreen();
            newY = e.getYOnScreen();
            //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
            cp.setBounds(startX + (newX - oldX), startY + (newY - oldY), cp.getWidth(), cp.getHeight());
        }

    }
}