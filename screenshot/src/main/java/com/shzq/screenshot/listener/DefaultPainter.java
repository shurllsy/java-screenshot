package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.enums.States;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * 区域截取
 *
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public class DefaultPainter extends Painter {

    //表示八个编辑点的区域
    private Rectangle[] rec;
    // 鼠标按下的坐标
    private Point pressedPoint = new Point();
    //下面四个常量,分别表示谁是被选中的那条线上的端点
    public static final int START_X = 1;
    public static final int START_Y = 2;
    public static final int END_X = 3;
    public static final int END_Y = 4;
    // 表示当前的编辑状态
    public States current = States.DEFAULT;
    //当前被选中的X和Y,只有这两个需要改变
    public int currentX, currentY;

    //表示一般情况下的鼠标状态
    private Cursor defaultCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

    public DefaultPainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
        initLayer();
        initRecs();
    }

    private void initRecs() {
        rec = new Rectangle[8];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = new Rectangle();
        }
    }

    private void initLayer() {
        // 带阴影的图层
        RescaleOp rescaleOp = new RescaleOp(0.6f, 0, null);
        bufferImage = rescaleOp.filter(imagePanel.image, null);
    }

    @Override
    public void pressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressedPoint.setLocation(e.getX(), e.getY());
            tools.setVisible(false);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            parent.dispose();
        }
    }

    @Override
    public void released(MouseEvent e) {
        // 设置工具条位置
        MyRectangle outRectangle = imagePanel.getSelectedRectangle();
        tools.moveTo(tools.getToolsLocationPoint(outRectangle));
        if (e.isPopupTrigger()) {
            if (current == States.MOVE) {
                outRectangle.setStartX(0);
                outRectangle.setStartY(0);
                outRectangle.setEndX(0);
                outRectangle.setEndY(0);
                imagePanel.repaint();
            } else {
                parent.dispose();
            }
        }
    }

    @Override
    public void clicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();
            if (imagePanel.getSelect().contains(p)) {
                BufferedImage selectedImg = imagePanel.getSelectedImg();
                tools.copyToClipboard(selectedImg);
                parent.dispose();
            }
        }
    }

    @Override
    public void moved(MouseEvent e) {
        doMouseMoved(e);
        initSelect(current);
    }

    @Override
    public void dragged(MouseEvent e) {
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();
        // 鼠标在X轴拖动距离，往右为正，往左为负
        int movedX = e.getX() - pressedPoint.x;
        // 鼠标在Y轴拖动的距离，往下为正，往上为负
        int movedY = e.getY() - pressedPoint.y;
        if (current == States.MOVE) {
            moveSelectedRectangle(e.getPoint());
        } else if (current == States.EAST || current == States.WEST) {
            if (currentX == START_X) {
                selectedRectangle.incrementStartX(movedX);
            } else {
                selectedRectangle.incrementEndX(movedX);
            }
            pressedPoint.x = e.getX();
        } else if (current == States.NORTH || current == States.SOUTH) {
            if (currentY == START_Y) {
                selectedRectangle.incrementStartY(movedY);
            } else {
                selectedRectangle.incrementEndY(movedY);
            }
            pressedPoint.y = e.getY();
        } else if (current == States.NORTH_WEST || current == States.NORTH_EAST ||
                current == States.SOUTH_EAST || current == States.SOUTH_WEST) {
            if (currentY == START_Y) {
                selectedRectangle.incrementStartY(movedY);
            } else {
                selectedRectangle.incrementEndY(movedY);
            }
            if (currentX == START_X) {
                selectedRectangle.incrementStartX(movedX);
            } else {
                selectedRectangle.incrementEndX(movedX);
            }
            pressedPoint.setLocation(e.getPoint());
        } else {
            selectedRectangle.reset(pressedPoint.x, pressedPoint.y, e.getX(), e.getY());
        }
        calcRec(selectedRectangle);

        imagePanel.setSelect(selectedRectangle.toRectangle());
        imagePanel.repaint();
    }

    public void drawImg(Graphics bufferImageGraphics) {
        bufferImageGraphics.setColor(Color.decode("#1EA4FF"));
        // 选择的区域矩形框
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();

        int selectWidth = selectedRectangle.getDimension().width;
        int selectHeight = selectedRectangle.getDimension().height;
        //画框x、y轴中点
        int midpointX = selectedRectangle.getStartX() + selectWidth / 2;
        int midpointY = selectedRectangle.getStartY() + selectHeight / 2;

        if (selectWidth > 0 && selectHeight > 0) {
            // 画框内取消阴影效果
            restoreRescale(bufferImageGraphics, selectedRectangle);
        }

        // 绘制画框边上的八个改变大小的红方块
        drawEditBlockOnLine(bufferImageGraphics, midpointX, midpointY);
    }

    //特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
    private void doMouseMoved(MouseEvent e) {
        //判断编辑后不能再移动
        if (imagePanel.getSelect().contains(e.getPoint())) {
            imagePanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            current = States.MOVE;
        } else {
            States[] st = States.values();
            for (int i = 0; i < rec.length; i++) {
                if (rec[i].contains(e.getPoint())) {
                    current = st[i];
                    imagePanel.setCursor(st[i].getCursor());
                    return;
                }
            }
            current = States.DEFAULT;
            imagePanel.setCursor(defaultCursor);
        }
    }

    //根据东南西北等八个方向决定选中的要修改的X和Y的座标
    private void initSelect(States state) {
        switch (state) {
            case EAST:
                currentX = END_X;
                currentY = 0;
                break;
            case WEST:
                currentX = START_X;
                currentY = 0;
                break;
            case NORTH:
                currentX = 0;
                currentY = START_Y;
                break;
            case SOUTH:
                currentX = 0;
                currentY = END_Y;
                break;
            case NORTH_EAST:
                currentX = END_X;
                currentY = START_Y;
                break;
            case NORTH_WEST:
                currentX = START_X;
                currentY = START_Y;
                break;
            case SOUTH_EAST:
                currentX = END_X;
                currentY = END_Y;
                break;
            case SOUTH_WEST:
                currentX = START_X;
                currentY = END_Y;
                break;
            default:
                currentX = 0;
                currentY = 0;
                break;
        }
    }

    /**
     * 复原截图区域的蒙版效果
     * 目前是通过在区域上覆盖原图相同区域实现的
     *
     * @param g 截图区域的Graphics
     */
    private void restoreRescale(Graphics g, MyRectangle rectangle) {
        int startX = rectangle.getStartX();
        int startY = rectangle.getStartY();
        Dimension dimension = rectangle.getDimension();
        imagePanel.selectAreaImage = imagePanel.image.getSubimage(startX, startY, dimension.width, dimension.height);
        imagePanel.selectAreaImageCache = PainterUtil.createCompatibleImage(dimension.width, dimension.height, imagePanel.selectAreaImage.getType());
        Graphics selectAreaImageCacheGraphics = imagePanel.selectAreaImageCache.getGraphics();
        selectAreaImageCacheGraphics.drawImage(imagePanel.selectAreaImage, 0, 0, null);
        selectAreaImageCacheGraphics.dispose();
        g.drawImage(imagePanel.selectAreaImage, startX, startY, null);
    }

    /**
     * 线上 编辑的点
     *
     * @param g         Graphics
     * @param midpointX x中点
     * @param midpointY y中点
     */
    private void drawEditBlockOnLine(Graphics g, int midpointX, int midpointY) {
        int startX = imagePanel.getSelectedRectangle().getStartX();
        int startY = imagePanel.getSelectedRectangle().getStartY();
        int endX = imagePanel.getSelectedRectangle().getEndX();
        int endY = imagePanel.getSelectedRectangle().getEndY();
        g.fillRect(midpointX - 2, startY - 2, 5, 5);
        g.fillRect(midpointX - 2, endY - 2, 5, 5);
        g.fillRect(startX - 2, midpointY - 2, 5, 5);
        g.fillRect(endX - 2, midpointY - 2, 5, 5);
        g.fillRect(startX - 2, startY - 2, 5, 5);
        g.fillRect(startX - 2, endY - 2, 5, 5);
        g.fillRect(endX - 2, startY - 2, 5, 5);
        g.fillRect(endX - 2, endY - 2, 5, 5);
    }

    private void moveSelectedRectangle(Point eventPoint) {
        //控制选框只能在屏幕范围内拖动，并且保持大小不异常改变
        MyRectangle outRectangle = imagePanel.getSelectedRectangle();
        // 鼠标在X轴拖动距离，往右为正，往左为负
        int movedX = eventPoint.x - pressedPoint.x;
        // 鼠标在Y轴拖动的距离，往下为正，往上为负
        int movedY = eventPoint.y - pressedPoint.y;
        int tmpStartX = outRectangle.getStartX() + movedX;
        int tmpStartY = outRectangle.getStartY() + movedY;
        int tmpEndX = outRectangle.getEndX() + movedX;
        int tmpEndY = outRectangle.getEndY() + movedY;

        // ww = windowWidth， wh = windowHeight
        int ww = parent.winDi.width;
        int wh = parent.winDi.height;
        if (tmpStartX < 0) {
            tmpEndX += Math.abs(tmpStartX);
            tmpStartX = 0;
        }
        if (tmpStartY < 0) {
            tmpEndY += Math.abs(tmpStartY);
            tmpStartY = 0;
        }
        if (tmpEndX > ww) {
            tmpStartX -= (tmpEndX - ww);
            tmpEndX = ww;
        }
        if (tmpEndY > wh) {
            tmpStartY -= (tmpEndY - wh);
            tmpEndY = wh;
        }

        outRectangle.setStartX(tmpStartX);
        outRectangle.setEndX(tmpEndX);
        outRectangle.setStartY(tmpStartY);
        outRectangle.setEndY(tmpEndY);

        pressedPoint.setLocation(eventPoint);
    }

    /**
     * 计算八个改变大小拖动的
     *
     * @param selectedRectangle 选框矩形
     */
    private void calcRec(MyRectangle selectedRectangle) {
        int startX = selectedRectangle.getStartX();
        int startY = selectedRectangle.getStartY();
        int endX = selectedRectangle.getEndX();
        int endY = selectedRectangle.getEndY();
        Dimension selectedRectangleDimension = selectedRectangle.getDimension();
        int selectWidth = selectedRectangleDimension.width;
        int selectHeight = selectedRectangleDimension.height;
        // 选择区域的大小
        //画框x、y轴中点
        int midpointX = startX + selectWidth / 2;
        int midpointY = startY + selectHeight / 2;
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

}
