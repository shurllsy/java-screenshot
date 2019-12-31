package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.enums.States;
import com.shzq.screenshot.utils.PainterUtil;

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
    private BufferedImage image;
    // 加了阴影的图层
    private BufferedImage reScaleImage;

    public Graphics selectAreaGraphics;
    // 标记功能，主要是修改该图，最后将该图draw到选框范围内
    public BufferedImage selectAreaImage;
    public BufferedImage selectAreaImageCache;

    private Dimension winDi;
    // 选择的区域
    private MyRectangle selectedRectangle;
    private Rectangle select = new Rectangle(0, 0, 0, 0);//表示选中的区域
    public States current = States.DEFAULT;// 表示当前的编辑状态
    private Rectangle[] rec;//表示八个编辑点的区域

    public ImageBufferPanel(BufferedImage image, Dimension winDi) {
        selectedRectangle = new MyRectangle();
        this.image = image;
        this.winDi = winDi;

        initRecs();
        initLayer();
    }

    private void initRecs() {
        rec = new Rectangle[8];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = new Rectangle();
        }
    }

    private void initLayer() {
        // 带阴影的图层
        RescaleOp rescaleOp = new RescaleOp(0.7f, 0, null);
        this.reScaleImage = rescaleOp.filter(image, null);
    }

    public void setCurrent(States states) {
        this.current = states;
    }

    public States getCurrent() {
        return this.current;
    }

    /**
     * 复原截图区域的蒙版效果
     * 目前是通过在区域上覆盖原图相同区域实现的
     *
     * @param g 截图区域的Graphics
     */
    public void restoreRescale(Graphics g, MyRectangle rectangle) {
        int startX = rectangle.getStartX();
        int startY = rectangle.getStartY();
        Dimension dimension = rectangle.getDimension();
        selectAreaImage = image.getSubimage(startX, startY, dimension.width, dimension.height);
        selectAreaImageCache = new BufferedImage(dimension.width, dimension.height, selectAreaImage.getType());
        selectAreaImageCache.setData(selectAreaImage.getData());
        g.drawImage(selectAreaImage, startX, startY, this);
    }

    public void refreshSelectArea(){
        Graphics graphics = getGraphics();
        graphics.drawImage(selectAreaImage, selectedRectangle.getStartX(), selectedRectangle.getStartY(), this);
    }

    public void paintComponent(Graphics g) {
        Dimension outRectangleDimension = selectedRectangle.getDimension();
        int startX = selectedRectangle.getStartX();
        int startY = selectedRectangle.getStartY();
        int endX = selectedRectangle.getEndX();
        int endY = selectedRectangle.getEndY();
        int selectWidth = outRectangleDimension.width;
        int selectHeight = outRectangleDimension.height;

        g.drawImage(reScaleImage, 0, 0, winDi.width, winDi.height, this);
        if (selectWidth > 0 && selectHeight > 0) {
            // 画框内取消阴影效果
            restoreRescale(g, selectedRectangle);
            selectAreaGraphics = selectAreaImage.getGraphics();
        }
        select = selectedRectangle.toRectangle();

        //画框x、y轴中点
        int midpointX = startX + selectWidth / 2;
        int midpointY = startY + selectHeight / 2;
        g.setColor(Color.RED);
        // 绘制画框的边线
        drawOutline(g);

        // 绘制画框边上的八个改变大小的红方块
        drawEditBlockOnLine(g, midpointX, midpointY);

        // 计算八个编辑点的区域坐标，并保存至rec，用于编辑大小时 识别是哪个点，以便调整大小计算
        rec[0] = new Rectangle(startX - 5, startY - 5, 10, 10);
        rec[1] = new Rectangle(midpointX - 5, startY - 5, 10, 10);
        rec[2] = new Rectangle(endX - 5, startY - 5, 10, 10);
        rec[3] = new Rectangle(endX - 5, midpointY - 5, 10, 10);
        rec[4] = new Rectangle(endX, endY - 5, 10, 10);
        rec[5] = new Rectangle(midpointX - 5, endY - 5, 10, 10);
        rec[6] = new Rectangle(startX - 5, endY - 5, 10, 10);
        rec[7] = new Rectangle(startX - 5, midpointY - 5, 10, 10);
    }

    /**
     * 画外边线
     */
    public void drawOutline(Graphics g) {
        PainterUtil.drawRectangle(selectedRectangle, g);
    }

    public void drawEditBlockOnLine(Graphics g, int midpointX, int midpointY) {
        int startX = selectedRectangle.getStartX();
        int startY = selectedRectangle.getStartY();
        int endX = selectedRectangle.getEndX();
        int endY = selectedRectangle.getEndY();
        g.fillRect(midpointX - 2, startY - 2, 5, 5);
        g.fillRect(midpointX - 2, endY - 2, 5, 5);
        g.fillRect(startX - 2, midpointY - 2, 5, 5);
        g.fillRect(endX - 2, midpointY - 2, 5, 5);
        g.fillRect(startX - 2, startY - 2, 5, 5);
        g.fillRect(startX - 2, endY - 2, 5, 5);
        g.fillRect(endX - 2, startY - 2, 5, 5);
        g.fillRect(endX - 2, endY - 2, 5, 5);
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

    public Rectangle[] getRec() {
        return rec;
    }

}
