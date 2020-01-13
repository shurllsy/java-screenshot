package com.shzq.screenshot.listener;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.utils.PainterUtil;
import com.shzq.screenshot.view.ImageBufferPanel;
import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * 标注画笔
 * <p>
 * imagePanel.appliedImage为当前截图显示的图（带scale）
 * 每个Painter标注之前先复制到bufferImage，painter操作bufferImage，最终将bufferImage绘到panel
 * draw之前先用appliedImage的数据覆盖bufferImage，用来擦除轨迹，显示最新结果
 * <p>
 * 其他标注类（矩形、直线）：
 * .标注先画到selectAreaImage（selectAreaImageCache保存画之前的图数据，用来擦除轨迹，显示最新结果）
 * .selectAreaImage填充到bufferImage
 *
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public abstract class Painter implements MouseListener, MouseMotionListener {

    private boolean activated;
    protected ScreenFrame parent;
    protected ImageBufferPanel imagePanel;
    protected ToolsBar tools;
    protected Cursor painterCursor = Cursor.getDefaultCursor();

    protected BufferedImage bufferImage;

    public Painter(ScreenFrame parent, ToolsBar tools) {
        this.parent = parent;
        imagePanel = parent.getImagePanel();
        this.tools = tools;
    }

    public final void draw(Graphics g) {
        // 选择的区域矩形框
        MyRectangle selectedRectangle = imagePanel.getSelectedRectangle();

        int selectWidth = selectedRectangle.getDimension().width;
        int selectHeight = selectedRectangle.getDimension().height;

        //重置显示
        BufferedImage ipImg = imagePanel.getAppliedImage();
        bufferImage = PainterUtil.createCompatibleImage(ipImg.getWidth(), ipImg.getHeight(), ipImg.getType());
        Graphics bufferImageGraphics = bufferImage.getGraphics();
//        bufferImageGraphics.drawImage(ipImg, 0, 0, null);
        // 子类实现
        this.drawImg(bufferImageGraphics);

        if (selectWidth > 0 && selectHeight > 0) {
            Graphics graphics = bufferImageGraphics.create();
            graphics.setColor(Color.decode("#1EA4FF"));
            // 绘制画框的边线
            PainterUtil.drawRectangle(imagePanel.getSelectedRectangle(), graphics);
            graphics.dispose();
        }
        bufferImageGraphics.dispose();
//        g.drawImage(bufferImage, 0, 0, parent.winDi.width, parent.winDi.height, null);
    }

    protected abstract void drawImg(Graphics bufferImageGraphics);

    /**
     * 公共，双击保存
     *
     * @param e MouseEvent
     */
    @Override
    public final void mouseClicked(MouseEvent e) {
        clicked(e);
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();
            if (imagePanel.getSelect().contains(p)) {
                BufferedImage selectedImg = imagePanel.getSelectedImg();
                tools.copyToClipboard(selectedImg);
                parent.dispose();
            }
        }
    }

    /**
     * 公共，右键退出
     *
     * @param e MouseEvent
     */
    @Override
    public final void mousePressed(MouseEvent e) {
        imagePanel.requestFocus();
        pressed(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
            parent.dispose();
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        released(e);
    }

    public void applyBufferImage() {
        Optional.ofNullable(bufferImage).ifPresent(imagePanel::setAppliedImage);
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        entered(e);
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        exited(e);
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        dragged(e);
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        moved(e);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void clicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void pressed(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void released(MouseEvent e) {
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void entered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void exited(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     */
    public void dragged(MouseEvent e) {
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public void moved(MouseEvent e) {
    }

    /**
     * 被激活，选中时触发
     */
    public void activate() {
        this.activated = true;
        imagePanel.setCursor(painterCursor);
    }

    /**
     * 被取消激活，被取消选中时触发
     */
    public void inactivate() {
        this.activated = false;
    }

    public boolean isActivated() {
        return activated;
    }
}
