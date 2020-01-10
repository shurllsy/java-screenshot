package com.shzq.screenshot.bean;

import com.shzq.screenshot.enums.ResultType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 操作结束返回结果
 *
 * @author lianbo.zhang
 * @date 2020/1/10
 */
public class Result {

    /**
     * 操作类型
     */
    private ResultType resultType;

    /**
     * 成功与否
     */
    private boolean success;

    /**
     * 截屏图像对象
     */
    private BufferedImage image;

    /**
     * 截屏图像对象的byte[]形式
     */
    private byte[] imageBytes;

    public Result(ResultType resultType, boolean success, BufferedImage image) {
        this.resultType = resultType;
        this.success = success;
        try {
            if (image != null) {
                this.image = image;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", out);
                imageBytes = out.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultType getResultType() {
        return resultType;
    }

    public boolean isSuccess() {
        return success;
    }

    public BufferedImage getImage() {
        return image;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}
