package com.shzq.screenshot.test.v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class DrawTrail2 {
    BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    private DrawCanvas canvas = new DrawCanvas();
    private int preX = -1;
    private int preY = -1;
    private Image offScreenImage;  //图形缓存

    public void init() {
        g.fillRect(0, 0, 1600, 1000);
        JFrame frame = new JFrame("测试画出鼠标的轨迹");
        frame.setSize(600, 600);

        frame.add(canvas);
        frame.setLayout(null);
        canvas.setBounds(0, 0, 500, 500);
        frame.setVisible(true);

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (preX > 0 && preY > 0) {
                    g.setColor(Color.black);
                    g.drawLine(preX, preY, e.getX(), e.getY());
                }
                preX = e.getX();
                preY = e.getY();
                canvas.repaint();
            }
        });

        frame.addWindowListener(new WindowAdapter()//添加窗口关闭处理函数
        {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        DrawTrail2 dc = new DrawTrail2();
        dc.init();

    }

    class DrawCanvas extends Canvas {
        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, null);
        }

        @Override
        public void update(Graphics g) {
            if (offScreenImage == null)
                offScreenImage = this.createImage(500, 500);     //新建一个图像缓存空间,这里图像大小为800*600
            Graphics gImage = offScreenImage.getGraphics();  //把它的画笔拿过来,给gImage保存着
            paint(gImage);                                   //将要画的东西画到图像缓存空间去
            g.drawImage(offScreenImage, 0, 0, null);         //然后一次性显示出来
        }
    }
}