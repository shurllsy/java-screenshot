package com.shzq.screenshot.listener;

import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 文字标注
 *
 * @author lianbo.zhang
 * @date 2020/1/2
 */
public class TextPainter extends Painter {
    //    TextInput t = new TextInput();
    public TextPainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
    }

    @Override
    public void pressed(MouseEvent e) {
        Point clickPoint = e.getPoint();
//        TextInput input = new TextInput();
//        input.setBounds(200,200, 200, 100);
//
//        input.setVisible(true);
//        parent.getImagePanel().add(input);
//
//        t.setBounds(clickPoint.x-10, clickPoint.y-15, 200, 30);
//        imagePanel.add(t);
//        t.setVisible(true);
//        imagePanel.repaint();
    }

    @Override
    public void clicked(MouseEvent e) {


    }

    @Override
    public void draw(Graphics g) {
//        g.drawImage(imagePanel.getAppliedImage(), 0, 0, parent.winDi.width, parent.winDi.height, null);
//        applyBufferImage();
    }

    private class TextInput extends JTextArea {

    }
}
