package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import logic.OpeningExplorer;

public class TenkeyDrawer {
    private final OpeningScene scene;

    public TenkeyDrawer(OpeningScene scene) {
        this.scene = scene;
    }

    public void draw(Graphics2D g2d, int ox, int sw, int h, OpeningExplorer ex, int baseY) {
        Image lockImg = this.scene.getImgLock();
        if (lockImg != null) {
            int lw = 40;
            int lh = 40;
            g2d.drawImage(lockImg, ox + (sw - lw) / 2, baseY - 50, lw, lh, null);
        }
        
        int winX = ox + 15;
        int winW = sw - 30;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(winX, baseY, winW, 60);
        
        String err = ex.getErrorMessage();
        g2d.setColor("UNLOCKED!".equals(err) ? Color.CYAN : Color.GREEN);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        
        if (!err.isEmpty()) {
            g2d.drawString(err, winX + 10, baseY + 35);
        } else {
            String code = this.scene.getInputCode();
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < code.length(); i++) {
                stars.append("*");
            }
            g2d.drawString(stars.toString(), winX + 15, baseY + 40);
        }
        
        String[] keys = new String[]{"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "決定"};
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        for (int i = 0; i < keys.length; i++) {
            int r = i / 3;
            int c = i % 3;
            int bx = ox + 25 + c * 50;
            int by = baseY + 70 + r * 45;
            int bw = "決定".equals(keys[i]) ? 90 : 40;
            
            boolean isP = keys[i].equals(ex.getPressedKey());
            g2d.setColor(isP ? Color.GRAY : Color.LIGHT_GRAY);
            g2d.fill3DRect(bx, by, bw, 35, !isP);
            
            g2d.setColor(Color.BLACK);
            int off = isP ? 1 : 0;
            int textXOffset = keys[i].length() > 1 ? 15 : 12;
            g2d.drawString(keys[i], bx + textXOffset + off, by + 22 + off);
        }
    }
}
