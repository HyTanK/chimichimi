package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class RineIconDrawer {
    public static void draw(Graphics2D g2d, Rectangle r, boolean isP) {
        int x = r.x;
        int y = r.y;
        int w = r.width;
        int h = r.height;
        if (isP) {
            x += 5;
            y += 5;
            w -= 10;
            h -= 10;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(60, 196, 52));
        g2d.fillRoundRect(x, y, w, h, 20, 20);
        g2d.setColor(Color.WHITE);
        int ovalW = (int)((double)w * 0.8);
        int ovalH = (int)((double)h * 0.6);
        int ovalX = x + (w - ovalW) / 2;
        int ovalY = y + (h - ovalH) / 2 - (int)((double)h * 0.03);
        g2d.fillOval(ovalX, ovalY, ovalW, ovalH);
        int[] px = new int[]{ovalX + (int)((double)ovalW * 0.2), ovalX + (int)((double)ovalW * 0.1), ovalX + (int)((double)ovalW * 0.35)};
        int[] py = new int[]{ovalY + (int)((double)ovalH * 0.8), ovalY + (int)((double)ovalH * 1.1), ovalY + (int)((double)ovalH * 0.9)};
        g2d.fillPolygon(px, py, 3);
        g2d.setColor(new Color(60, 196, 52));
        g2d.setFont(new Font("Arial", 1, (int)((double)w * 0.32)));
        int textX = ovalX + (int)((double)ovalW * 0.08);
        int textY = ovalY + (int)((double)ovalH * 0.68);
        g2d.drawString("Rine", textX, textY);
    }
}
