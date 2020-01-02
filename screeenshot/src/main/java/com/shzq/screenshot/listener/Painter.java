package com.shzq.screenshot.listener;

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
 *
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public abstract class Painter implements MouseListener, MouseMotionListener {

    protected ScreenFrame parent;
    protected ImageBufferPanel imagePanel;
    protected ToolsBar tools;

    protected BufferedImage bufferedImage;

    public Painter(ScreenFrame parent, ToolsBar tools) {
        this.parent = parent;
        imagePanel = parent.getImagePanel();
        this.tools = tools;
    }

    public abstract void draw(Graphics g);

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
        pressed(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
            parent.dispose();
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        tools.setVisible(true);
        released(e);
    }

    public void applyBufferImage() {
        Optional.ofNullable(bufferedImage).ifPresent(imagePanel::setAppliedImage);
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
}
