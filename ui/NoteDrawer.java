package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import logic.OpeningExplorer;

public class NoteDrawer {
    private final OpeningScene scene;
    private static final Color COLOR_NOTE_BG = new Color(245, 245, 220);

    public NoteDrawer(OpeningScene scene) {
        this.scene = scene;
    }

    public void draw(Graphics2D g2d, int width, int h, OpeningExplorer ex) {
        g2d.setColor(COLOR_NOTE_BG);
        g2d.fillRect(0, 0, width, h);
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Serif", Font.BOLD, 24));
        g2d.drawString("【 メ　モ 】", 20, 40);
        g2d.drawLine(20, 50, width - 20, 50);
        
        int x = width - 80;
        g2d.setFont(new Font("Serif", Font.PLAIN, 16));
        for (String s : this.scene.getNoteList()) {
            this.drawVerticalString(g2d, "・" + s, x, 80);
            x -= 30;
        }
        
        String typing = ex.getCurrentTypingText();
        if (!typing.isEmpty()) {
            g2d.setColor(Color.BLUE);
            this.drawVerticalString(g2d, "・" + typing + "✍", x, 80);
        }
    }

    private void drawVerticalString(Graphics2D g2d, String text, int x, int y) {
        int currentY = y;
        for (int i = 0; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));
            g2d.drawString(c, x, currentY);
            currentY += 20;
        }
    }
}
