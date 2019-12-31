package com.shzq.screenshot.listener;

import com.shzq.screenshot.view.ScreenFrame;
import com.shzq.screenshot.view.ToolsBar;

import java.awt.*;

/**
 * @author lianbo.zhang
 * @date 2019/12/30
 */
public class LinePainter extends Painter {
    private Point pressedPoint = new Point();

    public LinePainter(ScreenFrame parent, ToolsBar tools) {
        super(parent, tools);
    }

}
