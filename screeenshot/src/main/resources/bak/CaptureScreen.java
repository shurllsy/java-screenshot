package com.shzq.screenshot.view;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class CaptureScreen {
    public void screen() {
        try {
            Robot ro = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension di = tk.getScreenSize();
            Rectangle rec = new Rectangle(0, 0, di.width, di.height);
            BufferedImage bi = ro.createScreenCapture(rec);
            JFrame jf = new JFrame();
            Temp temp = new Temp(jf, bi, di.width, di.height);
            jf.getContentPane().add(temp, BorderLayout.CENTER);
            jf.setUndecorated(true);
            jf.setSize(di);
            jf.setVisible(true);
            jf.setBounds(0, 0, di.width, di.height);
            jf.setAlwaysOnTop(true);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    /**
     * 公共的处理把当前的图片加入剪帖板的方法
     */
    public void doCopy(final BufferedImage image) {
        try {
            Transferable trans = new Transferable() {
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.imageFlavor};
                }

                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }

                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                    if (isDataFlavorSupported(flavor))
                        return image;
                    throw new UnsupportedFlavorException(flavor);
                }
            };
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    //一个暂时类，用于显示当前的屏幕图像
    private class Temp extends JPanel implements MouseListener, MouseMotionListener {
        private BufferedImage bi;
        private int width, height;
        private int startX, startY, endX, endY, pressedX, pressedY;
        private JFrame jf;
        private Rectangle select = new Rectangle(0, 0, 0, 0);//表示选中的区域
        private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);//表示一般情况下的鼠标状态
        private States current = States.DEFAULT;// 表示当前的编辑状态
        private Rectangle[] rec;//表示八个编辑点的区域
        //下面四个常量,分别表示谁是被选中的那条线上的端点
        public static final int START_X = 1;
        public static final int START_Y = 2;
        public static final int END_X = 3;
        public static final int END_Y = 4;
        private int currentX, currentY;//当前被选中的X和Y,只有这两个需要改变

        public Temp(JFrame jf, BufferedImage bi, int width, int height) {
            this.jf = jf;
            this.bi = bi;
            this.width = width;
            this.height = height;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            initRecs();
        }

        private void initRecs() {
            rec = new Rectangle[8];
            for (int i = 0; i < rec.length; i++) {
                rec[i] = new Rectangle();
            }
        }

        /**
         * 复原截图区域的蒙版效果
         * 目前是通过在区域上覆盖原图相同区域实现的
         *
         * @param g 截图区域的Graphics
         */
        public void restoreRescale(Graphics g, int x1, int y1, int x2, int y2) {
            int leftTopX = Math.min(x1, x2);
            int leftTopY = Math.min(y1, y2);
            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);
            BufferedImage subimage = bi.getSubimage(leftTopX, leftTopY, width, height);
            g.drawImage(subimage, leftTopX, leftTopY, this);
        }

        public void paintComponent(Graphics g) {
            int selectWidth = Math.abs(endX - startX);
            int selectHeight = Math.abs(endY - startY);

            // 带阴影的图层
            RescaleOp ro = new RescaleOp(0.7f, 0, null);
            BufferedImage filter = ro.filter(bi, null);
            g.drawImage(filter, 0, 0, width, height, this);
            if (selectWidth > 0 && selectHeight > 0 && startX >= 0 && startY >= 0) {
                // 画框内取消阴影效果
                restoreRescale(g, startX, startY, endX, endY);
            }

            g.setColor(Color.RED);
            // 画框的边线
            g.drawLine(startX, startY, endX, startY);
            g.drawLine(startX, endY, endX, endY);
            g.drawLine(startX, startY, startX, endY);
            g.drawLine(endX, startY, endX, endY);

            int x = Math.min(startX, endX);
            int y = Math.min(startY, endY);
            select = new Rectangle(x, y, selectWidth, selectHeight);
            int x1 = (startX + endX) / 2;
            int y1 = (startY + endY) / 2;

            //画框边上的八个改变大小的红方块
            g.fillRect(x1 - 2, startY - 2, 5, 5);
            g.fillRect(x1 - 2, endY - 2, 5, 5);
            g.fillRect(startX - 2, y1 - 2, 5, 5);
            g.fillRect(endX - 2, y1 - 2, 5, 5);
            g.fillRect(startX - 2, startY - 2, 5, 5);
            g.fillRect(startX - 2, endY - 2, 5, 5);
            g.fillRect(endX - 2, startY - 2, 5, 5);
            g.fillRect(endX - 2, endY - 2, 5, 5);

            //八个编辑点的区域坐标填充数据
            rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
            rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
            rec[2] = new Rectangle((Math.max(startX, endX)) - 5, y - 5, 10, 10);
            rec[3] = new Rectangle((Math.max(startX, endX)) - 5, y1 - 5, 10, 10);
            rec[4] = new Rectangle((Math.max(startX, endX)) - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[5] = new Rectangle(x1 - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[6] = new Rectangle(x - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);

        }

        //根据东南西北等八个方向决定选中的要修改的X和Y的座标
        private void initSelect(States state) {
            switch (state) {
                case EAST:
                    currentX = (endX > startX ? END_X : START_X);
                    currentY = 0;
                    break;
                case WEST:
                    currentX = (endX > startX ? START_X : END_X);
                    currentY = 0;
                    break;
                case NORTH:
                    currentX = 0;
                    currentY = (startY > endY ? END_Y : START_Y);
                    break;
                case SOUTH:
                    currentX = 0;
                    currentY = (startY > endY ? START_Y : END_Y);
                    break;
                case NORTH_EAST:
                    currentY = (startY > endY ? END_Y : START_Y);
                    currentX = (endX > startX ? END_X : START_X);
                    break;
                case NORTH_WEST:
                    currentY = (startY > endY ? END_Y : START_Y);
                    currentX = (endX > startX ? START_X : END_X);
                    break;
                case SOUTH_EAST:
                    currentY = (startY > endY ? START_Y : END_Y);
                    currentX = (endX > startX ? END_X : START_X);
                    break;
                case SOUTH_WEST:
                    currentY = (startY > endY ? START_Y : END_Y);
                    currentX = (endX > startX ? START_X : END_X);
                    break;
                default:
                    currentX = 0;
                    currentY = 0;
                    break;
            }
        }

        public void mouseMoved(MouseEvent me) {
            doMouseMoved(me);
            initSelect(current);
        }

        //特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
        private void doMouseMoved(MouseEvent me) {
            if (select.contains(me.getPoint())) {
                this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                current = States.MOVE;
            } else {
                States[] st = States.values();
                for (int i = 0; i < rec.length; i++) {
                    if (rec[i].contains(me.getPoint())) {
                        current = st[i];
                        this.setCursor(st[i].getCursor());
                        return;
                    }
                }
                this.setCursor(cs);
                current = States.DEFAULT;
            }
        }

        public void mouseExited(MouseEvent me) {
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseDragged(MouseEvent me) {
            // TODO 判断编辑了图层后 控制不能再更改大小或者拖动

            int x = me.getX();
            int y = me.getY();
            if (current == States.MOVE) {
                // 鼠标在X轴拖动距离，往右为正，往左为负
                int movedX = x - pressedX;
                // 鼠标在Y轴拖动的距离，往下为正，往上为负
                int movedY = y - pressedY;

                //控制选框只能在屏幕范围内拖动，并且保持大小不异常改变
                int tmpStartX = startX + movedX;
                int tmpStartY = startY + movedY;
                int tmpEndX = endX + movedX;
                int tmpEndY = endY + movedY;
                if (tmpStartX >= 0 && tmpEndX <= width) {
                    startX = tmpStartX;
                    endX = tmpEndX;
                }
                if (tmpStartY >= 0 && tmpEndY <= height) {
                    startY = tmpStartY;
                    endY = tmpEndY;
                }

                pressedX = x;
                pressedY = y;
            } else if (current == States.EAST || current == States.WEST) {
                if (currentX == START_X) {
                    startX += (x - pressedX);
                } else {
                    endX += (x - pressedX);
                }
                pressedX = x;
            } else if (current == States.NORTH || current == States.SOUTH) {
                if (currentY == START_Y) {
                    startY += (y - pressedY);
                } else {
                    endY += (y - pressedY);
                }
                pressedY = y;
            } else if (current == States.NORTH_WEST || current == States.NORTH_EAST ||
                    current == States.SOUTH_EAST || current == States.SOUTH_WEST) {
                if (currentY == START_Y) {
                    startY += (y - pressedY);
                } else {
                    endY += (y - pressedY);
                }
                pressedY = y;
                if (currentX == START_X) {
                    startX += (x - pressedX);
                } else {
                    endX += (x - pressedX);
                }
                pressedX = x;
            } else {
                startX = pressedX;
                startY = pressedY;
                endX = me.getX();
                endY = me.getY();
            }
            this.repaint();
        }

        public void mousePressed(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                pressedX = me.getX();
                pressedY = me.getY();
            } else if (me.getButton() == MouseEvent.BUTTON3) {
                jf.dispose();
            }

        }

        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                if (current == States.MOVE) {
                    startX = 0;
                    startY = 0;
                    endX = 0;
                    endY = 0;
                    repaint();
                } else {
                    jf.dispose();
                }
            }
        }

        /**
         * 双击保存到剪贴板
         *
         * @param me MouseEvent
         */
        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
                Point p = me.getPoint();
                if (select.contains(p)) {
                    if (select.x + select.width < this.getWidth() && select.y + select.height < this.getHeight()) {
                        BufferedImage selectedImg = bi.getSubimage(select.x, select.y, select.width, select.height);
                        CaptureScreen.this.doCopy(selectedImg);
                    } else {
                        int wid = select.width, het = select.height;
                        if (select.x + select.width >= this.getWidth()) {
                            wid = this.getWidth() - select.x;
                        }
                        if (select.y + select.height >= this.getHeight()) {
                            het = this.getHeight() - select.y;
                        }
                        BufferedImage selectedImg = bi.getSubimage(select.x, select.y, wid, het);
                        CaptureScreen.this.doCopy(selectedImg);
                    }
                    jf.dispose();
                }
            }
        }
    }

    public static void main(String[] args) {
        CaptureScreen screen = new CaptureScreen();
        screen.screen();
    }

    //一些表示状态的枚举
    enum States {
        NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),//表示西北角
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
}

