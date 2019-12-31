package com.shzq.screenshot.test.v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class DrawTrail {
    BufferedImage image;
    Graphics g;
    Dimension ss;

    int sw = 1000;
    int sh = 1000;

    private Image offScreenImage;  //图形缓存

    public static void main(String[] args) throws AWTException {
        DrawTrail dc = new DrawTrail();
        dc.init();

    }

    public void init() throws AWTException {
        Robot ro = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit();
        ss = tk.getScreenSize();
        sw = ss.width -60;
        sh = ss.height -60;
        Rectangle rec = new Rectangle(0, 0, sw, sh);
        image = ro.createScreenCapture(rec);
//        image = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_RGB);
        g = image.getGraphics();
        g.fillRect(0, 0, sw, sh);
        JFrame frame = new JFrame("测试画出鼠标的轨迹");
        frame.setSize(sw, sh);
        DrawCanvas canvas = new DrawCanvas();
        frame.add(canvas);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
//        frame.setUndecorated(true);
        frame.setVisible(true);
//        frame.setLocation(0,50);


        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }


    class DrawCanvas extends JPanel {
        private static final long serialVersionUID = 1L;

        public DrawCanvas() {
            setBounds(0, 0, sw, sh);
            RectanglePainter rect = new RectanglePainter(this, g);
            addMouseMotionListener(rect);

        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, this);
//            g2.drawRect();
        }

        @Override
        public void update(Graphics g) {
            if (offScreenImage == null)
                offScreenImage = this.createImage(sw, sh);     //新建一个图像缓存空间,这里图像大小为800*600
            Graphics gImage = offScreenImage.getGraphics();  //把它的画笔拿过来,给gImage保存着
            paint(gImage);                                   //将要画的东西画到图像缓存空间去
            g.drawImage(offScreenImage, 0, 0, null);         //然后一次性显示出来
        }
    }
}