package ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class RoomDrawer {
    private final OpeningScene scene;
    private static final double SCALE_ROOM = 0.8;

    public RoomDrawer(OpeningScene scene) {
        this.scene = scene;
    }

    public void draw(Graphics2D g2d, int w, int h) {
        BufferedImage sheet = this.scene.getFullSheet();
        if (sheet == null) return;
        
        Rectangle r = this.scene.getCameraRect();
        int x = Math.max(0, Math.min(r.x, sheet.getWidth() - 1));
        int y = Math.max(0, Math.min(r.y, sheet.getHeight() - 1));
        int cw = Math.min(r.width, sheet.getWidth() - x);
        int ch = Math.min(r.height, sheet.getHeight() - y);
        
        if (cw <= 0 || ch <= 0) return;
        
        BufferedImage clip = sheet.getSubimage(x, y, cw, ch);
        int top = this.scene.getSceneMode() == 2 ? h / 8 + 40 : 0;
        
        double s = Math.min((double) w / clip.getWidth(), (double) (h - top) / clip.getHeight()) * SCALE_ROOM;
        int fw = (int) (clip.getWidth() * s);
        int fh = (int) (clip.getHeight() * s);
        g2d.drawImage(clip, (w - fw) / 2, top + (h - top - fh) / 2, fw, fh, null);
    }
}
