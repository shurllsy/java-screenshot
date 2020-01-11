package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 文字标注
 *
 * @author lianbo.zhang
 * @date 2020/1/2
 */
public class TextPainter extends Painter {
    private boolean globalEditing;
    private List<TextPane> textPanes = new ArrayList<>();
    // 文字输入框初始大小
    private Dimension inputInitDi = new Dimension(26, 36);

    // 红色边框
    private Border textBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 0, 0, 70)),
            BorderFactory.createEmptyBorder(7, 7, 7, 7));
    // 透明边框
    private Border noneBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 0)),
            BorderFactory.createEmptyBorder(7, 7, 7, 7));
    // 字体颜色
    private Color fontColor = Color.red;
    // 字体
    private Font inputFont = new Font("苹方", Font.PLAIN, 18);

    public TextPainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
        painterCursor = new Cursor(Cursor.TEXT_CURSOR);
    }

    @Override
    public void drawImg(Graphics bufferImageGraphics) {
        //将所有textPanel直接绘制到Cache， 初始化以
        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        Graphics selectAreaGraphics = selectAreaImage.createGraphics();
        selectAreaGraphics.drawImage(imagePanel.selectAreaImageCache, 0, 0, null);
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        drawAllText();
        PainterUtil.drawImage(selectedRectangle, selectAreaImage, bufferImageGraphics);
        selectAreaGraphics.dispose();
    }

    private void drawAllText() {
        BufferedImage selectAreaImage = imagePanel.selectAreaImage;
        Graphics selectAreaGraphics = selectAreaImage.createGraphics();
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        if (!globalEditing) {
            textPanes.forEach(tp -> {
                tp.setBorder(noneBorder);
                int x = tp.getX() - selectedRectangle.getStartX();
                int y = tp.getY() - selectedRectangle.getStartY();
                BufferedImage txt = PainterUtil.componentToImage(tp);
                if (tp.isEditing()) {
                    tp.setBorder(textBorder);
                }
                selectAreaGraphics.drawImage(txt, x, y, null);
            });
        }
        selectAreaGraphics.dispose();
    }

    @Override
    public void pressed(MouseEvent e) {
        Point clickPoint = e.getPoint();
        if (!globalEditing) {
            TextPane tp = new TextPane();
            tp.setBounds(clickPoint.x - 10, clickPoint.y - 15, inputInitDi.width, inputInitDi.height + 3);
            imagePanel.add(tp);
            textPanes.add(tp);
            globalEditing = true;
            tp.requestFocus();
            imagePanel.repaint();
        } else {
            imagePanel.requestFocus();
            globalEditing = false;
        }
    }

    @Override
    public void inactivate() {
        super.inactivate();
        globalEditing = false;
        // 将所有输入框置为失去焦点状态
        textPanes.stream()
                .filter(TextPane::isEditing)
                .findAny().ifPresent(TextPane::focusLost);

        // 从面板移除控件
        textPanes.forEach(imagePanel::remove);
        // 将控件的界面显示样子画到imagePanel.selectAreaImage imageBuffer
        // 不能直接调用imagePanel.repaint(); 因为它是异步的，可能textPanes.clear()之后才执行，导致文字全部消失
        drawAllText();
        // 清空textPanes，以便每个控件被回收
        textPanes.clear();
        // 将selectAreaImage应用到selectAreaImageCache
        Graphics graphics = imagePanel.selectAreaImageCache.getGraphics();
        graphics.drawImage(imagePanel.selectAreaImage, 0, 0, null);
        graphics.dispose();
        // 直接调用focusLost和drawAllText 文字的边框不会消失，需要重绘。
        imagePanel.repaint();
    }

    private void deleteInput(JTextPane textPane) {
        imagePanel.remove(textPane);
        globalEditing = false;
        textPanes.removeIf(tp -> tp == textPane);
        imagePanel.repaint();
    }

    private class TextPane extends JTextPane {
        // 是否处于编辑状态
        private boolean editing;

        public TextPane() {
            // textArea 设置
            setOpaque(false);
            // 光标颜色
            setCaretColor(Color.red);
            // 字体颜色
            setForeground(fontColor);
            setFont(inputFont);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    reSize();
                }
            });
            addFocusListener(new FocusListener(this));
            DragAdapter dragAdapter = new DragAdapter();
            addMouseListener(dragAdapter);
            addMouseMotionListener(dragAdapter);
        }

        public void reSize() {
            Dimension di = getPreferredSize();
            setSize(di.width + 10, di.height + 3);
        }

        private class FocusListener extends FocusAdapter {
            private JTextPane jp;

            public FocusListener(JTextPane sp) {
                this.jp = sp;
            }

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                jp.setBorder(textBorder);
                globalEditing = true;
                editing = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                TextPane.this.focusLost();
            }
        }

        public void focusLost() {
            editing = false;
            String text = getText();
            globalEditing = false;
            if ("".equals(text)) {
                TextPainter.this.deleteInput(this);
            } else {
                this.setBorder(noneBorder);
            }
        }

        public boolean isEditing() {
            return editing;
        }

        @Override
        public void print(Graphics g) {
            // 光标颜色
            setCaretColor(new Color(0, 0, 0, 0));
//            setBorder(noneBorder);
            try {
                super.print(g);
            } finally {
                setCaretColor(Color.red);
//                if (isEditing()) {
//                    setBorder(textBorder);
//                }
            }
        }
    }

}
