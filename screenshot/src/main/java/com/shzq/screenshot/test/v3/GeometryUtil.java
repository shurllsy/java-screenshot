package com.shzq.screenshot.test.v3;


import java.awt.geom.Point2D;

public class GeometryUtil {
    // 两点之间的距离
    public static double distanceOfPoints(Point2D p1, Point2D p2) {
        double disX = p2.getX() - p1.getX();
        double disY = p2.getY() - p1.getY();
        double dis = Math.sqrt(disX * disX + disY * disY);

        return dis;
    }

    // 两点的中点
    public static Point2D middlePoint(Point2D p1, Point2D p2) {
        double x = (p1.getX() + p2.getX()) / 2;
        double y = (p1.getY() + p2.getY()) / 2;

        return new Point2D.Double(x, y);
    }

    // 在两点所在直线上，以从startPoint到endPoint为方向，离startPoint的距离disToStartPoint的点
    public static Point2D extentPoint(Point2D startPoint, Point2D endPoint, double disToStartPoint) {
        double disX = endPoint.getX() - startPoint.getX();
        double disY = endPoint.getY() - startPoint.getY();
        double dis = Math.sqrt(disX * disX + disY * disY);
        double sin = (endPoint.getY() - startPoint.getY()) / dis;
        double cos = (endPoint.getX() - startPoint.getX()) / dis;
        double deltaX = disToStartPoint * cos;
        double deltaY = disToStartPoint * sin;

        return new Point2D.Double(startPoint.getX() + deltaX, startPoint.getY() + deltaY);
    }

    // 绕原点的旋转矩阵，绕任意点旋转，可以先移动到原点，旋转，然后再移回去
    // cosθ -sinθ 0
    // sinθ +conθ 0
    // 0000 +0000 1
    // x = r*cosα, y = r*sinα
    // x' = r*cos(α+θ) = r*cosα*cosθ - r*sinα*sinθ = x*cosθ - y*sinθ
    // y' = r*sin(α+θ) = r*sinα*cosθ + r*cosα*sinθ = x*sinθ + y*cosθ
    // (x, y)绕圆心旋转degree度
    public static Point2D rotate(double x, double y, double degree) {
        return rotate(x, y, 0, 0, degree);
    }

    // (x, y)绕(ox, oy)旋转degree度
    public static Point2D rotate(double x, double y, double ox, double oy, double degree) {
        x -= ox;
        y -= oy;

        double cos = Math.cos(Math.toRadians(degree));
        double sin = Math.sin(Math.toRadians(degree));

        double temp = x * cos - y * sin;
        y = x * sin + y * cos;
        x = temp;

        return new Point2D.Double(x + ox, y + oy);
    }

    public static void main(String[] args) {
        Point2D p = rotate(50, 10, 10);
        System.out.println(p);
        p = rotate(100, 60, 50, 50, 10);
        System.out.println(p);
    }
}