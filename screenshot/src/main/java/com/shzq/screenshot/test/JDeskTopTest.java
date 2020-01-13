package com.shzq.screenshot.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

public class JDeskTopTest extends JFrame {


    private ImageIcon icon = new ImageIcon(new Robot().createScreenCapture(new Rectangle(400, 400))); // 创建背景图片对象

    private JPanel panel = new JPanel();
    private JDesktopPane desktopPane = new JDesktopPane();//创建一个桌面面板对象
    private JLabel backLabel = new JLabel();
    private JButton buttonFirst = new JButton("按钮一");
    private JButton buttonSecond = new JButton("按钮二");
    private JButton buttonThird = new JButton("按钮三");

    private InternalFrame pInFrame = null;//定义一个按钮一内部窗体对象
    private InternalFrame rInFrame = null;//定义一个按钮二内部窗体对象
    private InternalFrame tInFrame = null;//定义一个按钮三内部窗体对象

    public JDeskTopTest() throws AWTException {
        setTitle("选项卡面板");
        setBounds(400, 400, 400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backLabel.setIcon(icon); //令标签组件显示背景图片
        backLabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());//设置标签组件的显示位置及大小

        desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);//设置内部窗体的拖动模式为拖动时连续重绘
        desktopPane.add(backLabel, new Integer(Integer.MIN_VALUE));//将标签组件添加到指定索引位置（最小值）

        final FlowLayout flowLayout = new FlowLayout();//设置JPanel面板的布局格式
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel.setLayout(flowLayout);

        //添加按钮事件监听器，并将按钮添加到面板上
        buttonFirst.addActionListener(new BAListener(pInFrame, "按钮一"));
        buttonSecond.addActionListener(new BAListener(rInFrame, "按钮二"));
        buttonThird.addActionListener(new BAListener(tInFrame, "按钮三"));
        panel.add(buttonFirst);
        panel.add(buttonSecond);
        panel.add(buttonThird);

        add(desktopPane);
        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }

    //内部类事件监听器
    private class BAListener implements ActionListener {

        InternalFrame inFrame;
        String title;

        //获取窗体对象以及窗体的标题
        public BAListener(InternalFrame inFrame, String title) {//
            this.inFrame = inFrame;
            this.title = title;
        }

        //事件处理
        public void actionPerformed(ActionEvent e) {

            if (inFrame == null || inFrame.isClosed()) {

                JInternalFrame[] allFrames = desktopPane.getAllFrames();//获得桌面面板中的所拥有内部窗体的数量

                int titleBarHight = 30 * allFrames.length;//设置窗体标题的宽度
                int x = 10 + titleBarHight;
                int y = x;// 设置窗体的显示位置
                int width = 250;
                int height = 180;// 设置窗体的大小

                inFrame = new InternalFrame(title);// 创建指定标题的内部窗体
                inFrame.setBounds(x, y, width, height);// 设置窗体的显示位置及大小
                inFrame.setVisible(true);// 设置窗体可见
                desktopPane.add(inFrame);// 将窗体添加到桌面面板中
            }

            try {

                inFrame.setSelected(true);//设置窗体被激活（选中窗体）

            } catch (PropertyVetoException propertyVetoE) {
                propertyVetoE.printStackTrace();
            }

        }
    }

    //对内部窗体的处理
    private class InternalFrame extends JInternalFrame {

        public InternalFrame(String title) {

            setTitle(title);// 设置内部窗体的标题
            setResizable(true);// 设置允许自由调整大小
            setClosable(true);// 设置提供关闭按钮
            setIconifiable(true);// 设置提供图标化按钮
            setMaximizable(true);// 设置提供最大化按钮

//            URL resource = this.getClass().getResource("/titleImagine.png");//获得图片的路径
//            ImageIcon icon = new ImageIcon(resource);//创建图片对象
//
//            setFrameIcon(icon); //设置窗体图标
        }
    }

    public static void main(String[] args) throws AWTException {
        // TODO Auto-generated method stub
        JDeskTopTest test = new JDeskTopTest();

    }

}
