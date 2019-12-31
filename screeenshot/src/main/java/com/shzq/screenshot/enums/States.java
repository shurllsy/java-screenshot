package com.shzq.screenshot.enums;

import java.awt.*;

/**
 * @author lianbo.zhang
 * @date 2019/12/27
 */
public enum States {
    /*
        顺序不能更改！！！
     */
    NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),//表示西北角,
    NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
    NORTH_EAST(new Cursor(Cursor.NE_RESIZE_CURSOR)),
    EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
    SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
    SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
    SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
    WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
    MOVE(new Cursor(Cursor.MOVE_CURSOR)),
    DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR));

    private Cursor cs;

    States(Cursor cs) {
        this.cs = cs;
    }

    public Cursor getCursor() {
        return cs;
    }
}
