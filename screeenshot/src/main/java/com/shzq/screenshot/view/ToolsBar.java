package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.MyRectangle;
import com.shzq.screenshot.listener.DefaultPainter;
import com.shzq.screenshot.listener.LinePainter;
import com.shzq.screenshot.listener.Painter;
import com.shzq.screenshot.listener.RectanglePainter;
import com.shzq.screenshot.view.component.ToolsButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lianbo.zhang
 * @date 2019/12/26
 */
public class ToolsBar extends JDialog {
    // 和截图区域的间距
    private int margin = 5;
    private ScreenFrame parent;
    private Painter defaultPainter;
    private Painter rectanglePainter;
    private Painter linePainter;

    private List<ToolsButton> tools;

    public ToolsBar(ScreenFrame parent) {
        // owner 用来保证工具条悬浮在截屏之上
        tools = new ArrayList<>();
        this.parent = parent;

        setUndecorated(true);
        setResizable(false);
        defaultPainter = new DefaultPainter(parent, this);
        parent.setPainter(defaultPainter);

        rectanglePainter = new RectanglePainter(parent, this);
        linePainter = new LinePainter(parent, this);

        init();
        pack();
    }

    public void moveTo(int x, int y) {
        setLocation(x + margin, y + margin);
        setVisible(true);
    }

    public void moveTo(Point p) {
        moveTo(p.x, p.y);
    }

    private void init() {
        JPanel panel = new Panel();

        // 直线
        ToolsButton lineButton = new ToolsButton(this, "line");
        lineButton.addActionListener(e -> {
            parent.setPainter(linePainter);
        });

        // 矩形画框
        ToolsButton selectButton = new ToolsButton(this, "select");
        selectButton.addActionListener(e -> {
            parent.setPainter(rectanglePainter);
        });

        // 保存下载
        ToolsButton saveButton = new ToolsButton(this, "save");
        saveButton.addActionListener(e -> {
            try {
                save(parent.getSelectedImg());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // 关闭
        ToolsButton closeButton = new ToolsButton(this, "close");
        closeButton.addActionListener(e -> {
            parent.dispose();
        });

        // 拷贝到剪贴板
        ToolsButton copyClipButton = new ToolsButton(this, "finish");
        copyClipButton.addActionListener(e -> {
            copyToClipboard(parent.getSelectedImg());
            parent.dispose();
        });

        // 分隔符
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(8, 23));
        sep.setBackground(Color.decode("#8b999b"));

        panel.add(selectButton);
        panel.add(lineButton);
        panel.add(sep);
        panel.add(saveButton);
        panel.add(closeButton);
        panel.add(copyClipButton);

        this.add(panel);
        this.setAlwaysOnTop(true);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            Point press = new Point();

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                press.setLocation(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                // 鼠标在X轴拖动距离，往右为正，往左为负
                int movedX = e.getX() - press.x;
                // 鼠标在Y轴拖动的距离，往下为正，往上为负
                int movedY = e.getY() - press.y;
                Point location = ToolsBar.this.getLocation();
                location.x += movedX;
                location.y += movedY;
                ToolsBar.this.setLocation(location);
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    /**
     * 保存图片
     *
     * @param image 需要保存的图
     * @throws IOException 错误
     */
    public void save(final BufferedImage image) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String fileName = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        parent.setAlwaysOnTop(false);
        setAlwaysOnTop(false);
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("保存");
        File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
        File defaultFile = new File(filePath + File.separator + fileName + ".png");
        jfc.setSelectedFile(defaultFile);
        int flag = jfc.showSaveDialog(parent);
        if (flag == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String path = file.getPath();
            // 检查文件后缀，防止用户忘记输入后缀或者输入不正确的后缀
            if (!(path.endsWith(".png") || path.endsWith(".PNG"))
                    && !(path.endsWith(".jpg") || path.endsWith(".JPG"))
                    && !(path.endsWith(".bmp") || path.endsWith(".BMP"))
                    && !(path.endsWith(".jpeg") || path.endsWith(".JPEG"))
                    && !(path.endsWith(".gif") || path.endsWith(".GIF"))) {
                path += ".png";
            }
            ImageIO.write(image, path.substring(path.lastIndexOf(".") + 1), new File(path));
        }
        parent.dispose();

    }

    /**
     * 把当前的图片加入剪帖板
     */
    public void copyToClipboard(final BufferedImage image) {
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

    // 判断工具栏边界,返回工具栏坐标
    public Point getToolsLocationPoint(int startX, int startY, int endX, int endY) {
        MyRectangle rectangle = new MyRectangle(startX, startY, endX, endY);
        return getToolsLocationPoint(rectangle);
    }

    public Point getToolsLocationPoint(MyRectangle rectangle) {
        Point p = new Point(rectangle.getRightBottom());
        p.x -= (getWidth() + 10);
        if (p.y + getHeight() + margin > parent.winDi.height) {
            // 超出屏幕最下角 展示在选框内
            p.y -= (getHeight() + margin + 5);
        }

        if (p.x < 0) {
            p.x = 0;
        }

        return p;
    }

    public List<ToolsButton> getTools() {
        return tools;
    }

    /**
     * 工具条Panel
     */
    private class Panel extends JPanel {

        public Panel() {
            FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 9, 1);
            setLayout(layout);
            setBorder(new EmptyBorder(5, 12, 5, 8));
        }

        @Override
        public Component add(Component comp) {
            if (comp instanceof ToolsButton) {
                tools.add((ToolsButton) comp);
            }
            return super.add(comp);
        }

    }
}
