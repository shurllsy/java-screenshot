package com.shzq.screenshot.test.v3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * @author lianbo.zhang
 * @date 2020/1/1
 */
public class MainFrame extends JFrame {
    private JPanel contentPane;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel jLabel1 = new JLabel(); //添加的jLabel 组件
    DrawPane drawPane = new DrawPane(); //创建DrawPane 的实例

    private void jbInit() {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(MainFrame.class.getResource("[Your Icon]")));
        contentPane = (JPanel) this.getContentPane();
        jLabel1.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("图像移动演示"); //标签的显示文本
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("JImageMoveDemo");
        contentPane.add(jLabel1, BorderLayout.NORTH);
        contentPane.add(this.drawPane, BorderLayout.CENTER);//添加drawPane 对象
        this.setVisible(true);
    }

    public MainFrame() {
        this.jbInit();
    }


    public static void main(String[] args) {
        new MainFrame();
    }
}


class DrawPane extends JPanel implements MouseMotionListener {
    private int x, y; //绘制图像的位置坐标
    BufferedImage bimage; //将显示的图像

    public DrawPane() {
        this.setBackground(Color.white); //背景色
        this.addMouseMotionListener(this); //添加鼠标移动监听器
        //加载图片
//        Image image = this.getToolkit().getImage(ClassLoader.getSystemResource("/icon/finish.png"));
        Image image = this.getToolkit().getImage(getClass().getResource("/icon/finish.png"));
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (Exception err) {
            err.printStackTrace();
        }
        if (image.getWidth(this) == -1) {
            System.out.println("Could not get the image");
            System.exit(-1);
        }
        //创建缓冲区图像
        bimage = new BufferedImage(image.getWidth(this), image.getHeight(this),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = bimage.createGraphics();
        g2D.drawImage(image, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(this.bimage, this.x, this.y, this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.x = e.getX(); //重新设置位置
        this.y = e.getY();
        repaint(); //重画
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}