package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import logic.OpeningExplorer;

public class OpeningPainter {
    private final OpeningScene scene;
    private final RoomDrawer roomDrawer;
    private final NoteDrawer noteDrawer;
    private final TenkeyDrawer tenkeyDrawer;
    private static final Color COLOR_LINE = new Color(0, 0, 0, 40);

    public OpeningPainter(OpeningScene scene) {
        this.scene = scene;
        this.roomDrawer = new RoomDrawer(scene);
        this.noteDrawer = new NoteDrawer(scene);
        this.tenkeyDrawer = new TenkeyDrawer(scene);
    }

    public void paint(Graphics2D g2d, int w, int h, OpeningExplorer explorer) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int mode = this.scene.getSceneMode();
        this.drawMainContent(g2d, w, h, mode, explorer);
        this.drawOverlayUI(g2d, w, h, mode);
    }

    private void drawMainContent(Graphics2D g2d, int w, int h, int mode, OpeningExplorer explorer) {
        if (mode < 2) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.scene.getAlpha()));
        }
        switch (mode) {
            case 0:
                this.drawLogo(g2d, w, h);
                break;
            case 1:
                this.roomDrawer.draw(g2d, w, h);
                if (this.scene.isShowOverlayTenkey()) {
                    this.tenkeyDrawer.draw(g2d, w / 2 - 90, 180, h, explorer, 450);
                }
                break;
            case 2:
                this.drawExplorationLayout(g2d, w, h, explorer);
                break;
            case 3:
                this.drawLockLayout(g2d, w, h, explorer);
                break;
            default:
                break;
        }
    }

    private void drawExplorationLayout(Graphics2D g2d, int w, int h, OpeningExplorer explorer) {
        int curX = this.scene.getCurImgX();
        int curW = this.scene.getCurFw();
        int rightX = curX + curW;
        
        this.noteDrawer.draw(g2d, curX, h, explorer);
        this.tenkeyDrawer.draw(g2d, rightX, w - rightX, h, explorer, 60);
        
        Graphics2D gRoom = (Graphics2D) g2d.create();
        gRoom.translate(curX, 0);
        this.roomDrawer.draw(gRoom, curW, h);
        explorer.drawEffect(gRoom);
        gRoom.dispose();
        
        g2d.setColor(COLOR_LINE);
        g2d.drawLine(curX, 0, curX, h);
        g2d.drawLine(rightX, 0, rightX, h);
    }

    private void drawLockLayout(Graphics2D g2d, int w, int h, OpeningExplorer explorer) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.scene.getAlpha()));
        BufferedImage bg = this.scene.getFullSheet();
        if (bg != null) {
            this.roomDrawer.draw(g2d, w, h);
        }
        int lw = 180;
        int lx = (w - lw) / 2;
        int ly = h / 2 - 100;
        this.tenkeyDrawer.draw(g2d, lx, lw, h, explorer, ly);
    }

    private void drawOverlayUI(Graphics2D g2d, int w, int h, int mode) {
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(Color.BLACK);
        String msg = this.scene.getDisplayMessage();
        if (mode == 1) {
            this.drawCenterText(g2d, msg, h / 12, 30, w);
        } else if (mode >= 2) {
            this.drawCenterText(g2d, msg, h / 10, mode == 3 ? 32 : 25, w);
        }
    }

    private void drawLogo(Graphics2D g2d, int w, int h) {
        Image logo = this.scene.getLogoTank();
        if (logo == null) {
            return;
        }
        int sw = logo.getWidth(null) / 3;
        int sh = logo.getHeight(null) / 3;
        g2d.drawImage(logo, (w - sw) / 2, (h - sh) / 2, sw, sh, null);
    }

    private void drawCenterText(Graphics2D g2d, String txt, int y, int size, int width) {
        if (txt == null || txt.isEmpty()) {
            return;
        }
        if (this.scene.isAlertMessage()) {
            g2d.setColor(new Color(255, 50, 50));
            g2d.setFont(new Font("SansSerif", Font.BOLD, (int) (size * 1.2)));
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Serif", Font.BOLD, size));
        }
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(txt, (width - fm.stringWidth(txt)) / 2, y);
    }
}
