package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.MyRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class ImageBufferPanel extends JPanel {
    // 原始截图
    public BufferedImage image;
    // 选择的区域
    private MyRectangle selectedRectangle;
    //表示选中的区域
    public Rectangle select = new Rectangle(0, 0, 0, 0);
    private ScreenFrame parent;

    /**
     * 已经应用到面板的图（全屏panel中显示的，每个painter作图完毕会将缓存刷新到该变量）
     */
    private BufferedImage appliedImage;
    // 画矩形等标记，主要是修改该图，最后将该图draw到选框范围内
    public BufferedImage selectAreaImage;
    public BufferedImage selectAreaImageCache;


    public ImageBufferPanel(ScreenFrame parent, BufferedImage image) {
        super(true);
        this.parent = parent;
        selectedRectangle = new MyRectangle();
        this.image = image;
        RescaleOp rescaleOp = new RescaleOp(0.6f, 0, null);
        appliedImage = rescaleOp.filter(image, null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        parent.getPainter().draw(g);
    }

    public BufferedImage getSelectedImg() {
        BufferedImage selectedImg;
        if (select.x + select.width < this.getWidth() && select.y + select.height < this.getHeight()) {
            selectedImg = image.getSubimage(select.x, select.y, select.width, select.height);
        } else {
            int wid = select.width, het = select.height;
            if (select.x + select.width >= this.getWidth()) {
                wid = this.getWidth() - select.x;
            }
            if (select.y + select.height >= this.getHeight()) {
                het = this.getHeight() - select.y;
            }
            selectedImg = image.getSubimage(select.x, select.y, wid, het);
        }
        return selectedImg;
    }

    public Rectangle getSelect() {
        return select;
    }

    public MyRectangle getSelectedRectangle() {
        return selectedRectangle;
    }

    public BufferedImage getAppliedImage() {
        return appliedImage;
    }

    public void setAppliedImage(BufferedImage appliedImage) {
        this.appliedImage = appliedImage;
    }
}
