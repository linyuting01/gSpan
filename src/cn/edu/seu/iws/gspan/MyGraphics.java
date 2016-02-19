/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.iws.gspan;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Nguyen Chau
 */
public class MyGraphics {

    Graphics g;
    Graphics2D g2d;

    public MyGraphics(Graphics g) {
        g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);
        
    }

    public static double KhoangCach(Point a, Point b) {
        return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
    }

    public void DrawPoint(Point[] luudiem, int index, String name, String picture) {
        int i = 0;
        Random rd = new Random();
        Point temp = new Point();
        while (i < luudiem.length) {
            temp.x = rd.nextInt(600);
            temp.y = rd.nextInt(400);
            if (Math.abs(temp.x - luudiem[i].x) < 50 || Math.abs(temp.y - luudiem[i].y) < 50 || KhoangCach(temp, luudiem[i]) < 50) {
                temp.x = rd.nextInt(600);
                temp.y = rd.nextInt(400);
                i = 0;
            } else {
                i++;
            }
        }
        BufferedImage img = null;
        try {
            if ((new File("images/" + picture).exists()) == false)
            {
                img = ImageIO.read(new File("images/user_face.png"));
            }
            else
            {
                img = ImageIO.read(new File("images/" + picture));
            }
        } catch (IOException e) {

        }
        g2d.setColor(Color.blue);
        g2d.drawImage(img, temp.x, temp.y, null);

        g2d.setFont(new Font(null, Font.BOLD, 15));
        g2d.drawString(name + "", temp.x, temp.y);

        GradientPaint grp = new GradientPaint(temp.x, temp.y, Color.blue,temp.x, temp.y, Color.blue);
        g2d.setPaint(grp);
        
        g2d.fillOval(temp.x, temp.y, 5, 5);
        luudiem[index] = temp;
    }

    public void VeCanh(Point p1, Point p2, String label) {
        g2d.setColor(Color.red);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(null, Font.BOLD, 12));
        g2d.drawString(label, (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }
}
